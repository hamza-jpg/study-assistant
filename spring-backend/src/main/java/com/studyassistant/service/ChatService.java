package com.studyassistant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyassistant.dto.chat.ChatMessageDto;
import com.studyassistant.dto.chat.ChatSessionDto;
import com.studyassistant.dto.chat.ChatStreamRequest;
import com.studyassistant.dto.rag.InternalRagStreamRequest;
import com.studyassistant.entity.ChatMessage;
import com.studyassistant.entity.ChatSession;
import com.studyassistant.entity.Course;
import com.studyassistant.entity.User;
import com.studyassistant.repository.ChatMessageRepository;
import com.studyassistant.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CourseService courseService;
    private final PythonRagClient pythonRagClient;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<ChatSessionDto> getUserSessions(UUID courseId, User user) {
        Course course = courseService.getCourseEntity(courseId);
        return chatSessionRepository.findByUserAndCourseOrderByUpdatedAtDesc(user, course).stream()
            .map(ChatSessionDto::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> getSessionMessages(UUID sessionId, User user) {
        ChatSession session = chatSessionRepository.findById(sessionId)
            .orElseThrow(() -> new IllegalArgumentException("Chat session not found: " + sessionId));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized to access session");
        }

        return chatMessageRepository.findBySessionOrderByCreatedAtAsc(session).stream()
            .map(ChatMessageDto::fromEntity)
            .toList();
    }

    @Transactional
    public Flux<ServerSentEvent<String>> streamChat(ChatStreamRequest request, User user) {
        Course course = courseService.getCourseEntity(request.getCourseId());

        // 1. Resolve or create chat session
        ChatSession session;
        if (request.getSessionId() != null) {
            session = chatSessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + request.getSessionId()));
            if (!session.getUser().getId().equals(user.getId())) {
                throw new org.springframework.security.access.AccessDeniedException("Unauthorized session access");
            }
        } else {
            String title = request.getQuery().trim();
            if (title.length() > 40) {
                title = title.substring(0, 37) + "...";
            }
            session = ChatSession.builder()
                .user(user)
                .course(course)
                .title(title)
                .build();
            session = chatSessionRepository.save(session);
        }

        // 2. Load prior conversation turns for conversational history
        List<ChatMessage> priorMessages = chatMessageRepository.findBySessionOrderByCreatedAtAsc(session);
        List<Map<String, String>> historyList = new ArrayList<>();
        for (ChatMessage msg : priorMessages) {
            historyList.add(Map.of("role", msg.getRole(), "content", msg.getContent()));
        }

        // 3. Save incoming user message
        ChatMessage userMsg = ChatMessage.builder()
            .session(session)
            .role("user")
            .content(request.getQuery().trim())
            .build();
        chatMessageRepository.save(userMsg);

        // Update session's updated_at
        session.setUpdatedAt(Instant.now());
        chatSessionRepository.save(session);

        // 4. Construct internal RAG request
        InternalRagStreamRequest ragRequest = InternalRagStreamRequest.builder()
            .query(request.getQuery().trim())
            .courseId(course.getId().toString())
            .history(historyList.isEmpty() ? null : historyList)
            .topK(request.getTopK())
            .topN(request.getTopN())
            .augment(request.getAugment())
            .useWeb(request.getUseWeb())
            .fallbackToWeb(request.getFallbackToWeb())
            .build();

        final UUID finalSessionId = session.getId();
        final long startTime = System.currentTimeMillis();
        final StringBuilder assistantReply = new StringBuilder();
        final AtomicReference<String> sourcesJsonRef = new AtomicReference<>("[]");

        // First emit session event so frontend can track session ID
        ServerSentEvent<String> initEvent = ServerSentEvent.<String>builder()
            .data("{\"type\": \"session\", \"sessionId\": \"" + finalSessionId + "\"}")
            .build();

        return Flux.concat(
            Flux.just(initEvent),
            pythonRagClient.streamRag(ragRequest)
                .doOnNext(sse -> {
                    String data = sse.data();
                    if (data != null && !data.isBlank()) {
                        try {
                            JsonNode node = objectMapper.readTree(data);
                            String type = node.path("type").asText();
                            if ("token".equals(type)) {
                                assistantReply.append(node.path("token").asText(""));
                            } else if ("done".equals(type)) {
                                JsonNode sourcesNode = node.path("sources");
                                if (!sourcesNode.isMissingNode() && sourcesNode.isArray()) {
                                    sourcesJsonRef.set(objectMapper.writeValueAsString(sourcesNode));
                                }
                            }
                        } catch (JsonProcessingException e) {
                            // Non-json or raw token data
                        }
                    }
                })
                .doFinally(signalType -> {
                    // 5. Persist assistant message upon stream completion
                    try {
                        String fullContent = assistantReply.toString().trim();
                        if (!fullContent.isEmpty()) {
                            int durationMs = (int) (System.currentTimeMillis() - startTime);
                            ChatSession currentSession = chatSessionRepository.findById(finalSessionId).orElse(null);
                            if (currentSession != null) {
                                ChatMessage assistantMsg = ChatMessage.builder()
                                    .session(currentSession)
                                    .role("assistant")
                                    .content(fullContent)
                                    .sources(sourcesJsonRef.get())
                                    .durationMs(durationMs)
                                    .build();
                                chatMessageRepository.save(assistantMsg);
                            }
                        }
                    } catch (Exception e) {
                        log.error("Failed to persist assistant chat message: {}", e.getMessage(), e);
                    }
                })
        );
    }
}

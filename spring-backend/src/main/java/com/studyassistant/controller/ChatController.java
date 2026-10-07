package com.studyassistant.controller;

import com.studyassistant.dto.chat.ChatMessageDto;
import com.studyassistant.dto.chat.ChatSessionDto;
import com.studyassistant.dto.chat.ChatStreamRequest;
import com.studyassistant.entity.User;
import com.studyassistant.service.AuthService;
import com.studyassistant.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final AuthService authService;

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(@Valid @RequestBody ChatStreamRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        return chatService.streamChat(request, user);
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<ChatSessionDto>> getSessions(@RequestParam UUID courseId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(chatService.getUserSessions(courseId, user));
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getSessionMessages(@PathVariable UUID sessionId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(chatService.getSessionMessages(sessionId, user));
    }
}

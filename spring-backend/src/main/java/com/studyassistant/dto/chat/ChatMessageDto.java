package com.studyassistant.dto.chat;

import com.studyassistant.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private UUID id;
    private UUID sessionId;
    private String role;
    private String content;
    private String sources;
    private Integer durationMs;
    private Instant createdAt;

    public static ChatMessageDto fromEntity(ChatMessage message) {
        return ChatMessageDto.builder()
            .id(message.getId())
            .sessionId(message.getSession().getId())
            .role(message.getRole())
            .content(message.getContent())
            .sources(message.getSources())
            .durationMs(message.getDurationMs())
            .createdAt(message.getCreatedAt())
            .build();
    }
}

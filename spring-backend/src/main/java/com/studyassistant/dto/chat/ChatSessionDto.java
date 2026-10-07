package com.studyassistant.dto.chat;

import com.studyassistant.entity.ChatSession;
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
public class ChatSessionDto {
    private UUID id;
    private UUID courseId;
    private String courseTitle;
    private String title;
    private Instant createdAt;
    private Instant updatedAt;

    public static ChatSessionDto fromEntity(ChatSession session) {
        return ChatSessionDto.builder()
            .id(session.getId())
            .courseId(session.getCourse().getId())
            .courseTitle(session.getCourse().getTitle())
            .title(session.getTitle())
            .createdAt(session.getCreatedAt())
            .updatedAt(session.getUpdatedAt())
            .build();
    }
}

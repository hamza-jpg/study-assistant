package com.studyassistant.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatStreamRequest {
    @NotBlank(message = "Query is required")
    private String query;

    @NotNull(message = "Course ID is required")
    private UUID courseId;

    private UUID sessionId;

    @Builder.Default
    private Integer topK = 15;

    @Builder.Default
    private Integer topN = 5;

    @Builder.Default
    private Boolean augment = true;

    @Builder.Default
    private Boolean useWeb = false;

    @Builder.Default
    private Boolean fallbackToWeb = true;
}

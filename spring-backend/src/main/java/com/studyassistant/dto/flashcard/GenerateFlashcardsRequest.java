package com.studyassistant.dto.flashcard;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateFlashcardsRequest {
    private UUID courseId;
    private String topic;
    private String deckTitle;

    @Min(value = 1, message = "Count must be at least 1")
    @Max(value = 15, message = "Count cannot exceed 15")
    @Builder.Default
    private Integer count = 5;
}

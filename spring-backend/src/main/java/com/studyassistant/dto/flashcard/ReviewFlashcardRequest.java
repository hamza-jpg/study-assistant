package com.studyassistant.dto.flashcard;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewFlashcardRequest {
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 (Again) and 4 (Easy)")
    @Max(value = 4, message = "Rating must be between 1 (Again) and 4 (Easy)")
    private Integer rating;
}

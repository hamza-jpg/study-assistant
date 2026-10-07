package com.studyassistant.dto.flashcard;

import com.studyassistant.entity.FlashcardReview;
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
public class FlashcardReviewDto {
    private UUID id;
    private UUID flashcardId;
    private UUID userId;
    private Integer rating;
    private Integer scheduledIntervalDays;
    private Double stability;
    private Double difficulty;
    private Instant nextReviewDate;
    private Instant reviewedAt;

    public static FlashcardReviewDto fromEntity(FlashcardReview review) {
        return FlashcardReviewDto.builder()
            .id(review.getId())
            .flashcardId(review.getFlashcard().getId())
            .userId(review.getUser().getId())
            .rating(review.getRating())
            .scheduledIntervalDays(review.getScheduledIntervalDays())
            .stability(review.getStability())
            .difficulty(review.getDifficulty())
            .nextReviewDate(review.getNextReviewDate())
            .reviewedAt(review.getReviewedAt())
            .build();
    }
}

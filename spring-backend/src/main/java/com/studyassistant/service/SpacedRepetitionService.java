package com.studyassistant.service;

import com.studyassistant.entity.Flashcard;
import com.studyassistant.entity.FlashcardReview;
import com.studyassistant.entity.User;
import com.studyassistant.repository.FlashcardReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpacedRepetitionService {

    private final FlashcardReviewRepository reviewRepository;

    @Transactional
    public FlashcardReview processReview(Flashcard flashcard, User user, int rating) {
        if (rating < 1 || rating > 4) {
            throw new IllegalArgumentException("Rating must be between 1 (Again) and 4 (Easy)");
        }

        Optional<FlashcardReview> latestReviewOpt = reviewRepository
            .findTopByFlashcardAndUserOrderByReviewedAtDesc(flashcard, user);

        int newInterval;
        double newDifficulty;
        double newStability;

        if (latestReviewOpt.isEmpty()) {
            // First time review
            switch (rating) {
                case 1 -> { // Again
                    newInterval = 1;
                    newDifficulty = 6.0;
                    newStability = 0.5;
                }
                case 2 -> { // Hard
                    newInterval = 1;
                    newDifficulty = 5.5;
                    newStability = 1.0;
                }
                case 3 -> { // Good
                    newInterval = 3;
                    newDifficulty = 5.0;
                    newStability = 2.5;
                }
                case 4 -> { // Easy
                    newInterval = 7;
                    newDifficulty = 4.0;
                    newStability = 4.0;
                }
                default -> throw new IllegalStateException("Unexpected rating: " + rating);
            }
        } else {
            FlashcardReview prior = latestReviewOpt.get();
            int priorInterval = Math.max(1, prior.getScheduledIntervalDays());
            double priorDiff = prior.getDifficulty();
            double priorStab = prior.getStability();

            switch (rating) {
                case 1 -> { // Again (lapse)
                    newInterval = 1;
                    newDifficulty = Math.min(10.0, priorDiff + 1.5);
                    newStability = Math.max(0.5, priorStab * 0.5);
                }
                case 2 -> { // Hard
                    newInterval = Math.max(1, (int) Math.round(priorInterval * 1.2));
                    newDifficulty = Math.min(10.0, priorDiff + 0.5);
                    newStability = priorStab * 1.1;
                }
                case 3 -> { // Good
                    double multiplier = Math.max(1.3, priorStab);
                    newInterval = Math.max(1, (int) Math.round(priorInterval * multiplier));
                    newDifficulty = priorDiff;
                    newStability = priorStab * 1.4;
                }
                case 4 -> { // Easy
                    double multiplier = Math.max(1.8, priorStab * 1.3);
                    newInterval = Math.max(2, (int) Math.round(priorInterval * multiplier));
                    newDifficulty = Math.max(1.0, priorDiff - 1.0);
                    newStability = priorStab * 1.8;
                }
                default -> throw new IllegalStateException("Unexpected rating: " + rating);
            }
        }

        Instant nextReviewDate = Instant.now().plus(Duration.ofDays(newInterval));

        FlashcardReview review = FlashcardReview.builder()
            .flashcard(flashcard)
            .user(user)
            .rating(rating)
            .scheduledIntervalDays(newInterval)
            .stability(Math.round(newStability * 100.0) / 100.0)
            .difficulty(Math.round(newDifficulty * 100.0) / 100.0)
            .nextReviewDate(nextReviewDate)
            .build();

        return reviewRepository.save(review);
    }
}

package com.studyassistant.service;

import com.studyassistant.entity.Course;
import com.studyassistant.entity.Flashcard;
import com.studyassistant.entity.FlashcardDeck;
import com.studyassistant.entity.FlashcardReview;
import com.studyassistant.entity.User;
import com.studyassistant.repository.FlashcardReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpacedRepetitionServiceTest {

    @Mock
    private FlashcardReviewRepository reviewRepository;

    @InjectMocks
    private SpacedRepetitionService spacedRepetitionService;

    private User testUser;
    private Flashcard testCard;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(UUID.randomUUID())
            .email("student@test.local")
            .build();

        Course course = Course.builder()
            .id(UUID.randomUUID())
            .code("CS101")
            .title("Computer Science 101")
            .build();

        FlashcardDeck deck = FlashcardDeck.builder()
            .id(UUID.randomUUID())
            .course(course)
            .title("Data Structures")
            .build();

        testCard = Flashcard.builder()
            .id(UUID.randomUUID())
            .deck(deck)
            .question("What is a Hash Map?")
            .answer("A data structure implementing an associative array")
            .build();

        when(reviewRepository.save(any(FlashcardReview.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("First review with 'Again' (rating 1) sets 1 day interval and decreases stability")
    void testFirstReviewAgain() {
        when(reviewRepository.findTopByFlashcardAndUserOrderByReviewedAtDesc(testCard, testUser))
            .thenReturn(Optional.empty());

        FlashcardReview review = spacedRepetitionService.processReview(testCard, testUser, 1);

        assertThat(review.getScheduledIntervalDays()).isEqualTo(1);
        assertThat(review.getRating()).isEqualTo(1);
        assertThat(review.getStability()).isEqualTo(0.5);
        assertThat(review.getDifficulty()).isEqualTo(6.0);
        assertThat(review.getNextReviewDate()).isAfter(Instant.now());
    }

    @Test
    @DisplayName("First review with 'Good' (rating 3) sets 3 days interval")
    void testFirstReviewGood() {
        when(reviewRepository.findTopByFlashcardAndUserOrderByReviewedAtDesc(testCard, testUser))
            .thenReturn(Optional.empty());

        FlashcardReview review = spacedRepetitionService.processReview(testCard, testUser, 3);

        assertThat(review.getScheduledIntervalDays()).isEqualTo(3);
        assertThat(review.getStability()).isEqualTo(2.5);
        assertThat(review.getDifficulty()).isEqualTo(5.0);
    }

    @Test
    @DisplayName("First review with 'Easy' (rating 4) sets 7 days interval and lower difficulty")
    void testFirstReviewEasy() {
        when(reviewRepository.findTopByFlashcardAndUserOrderByReviewedAtDesc(testCard, testUser))
            .thenReturn(Optional.empty());

        FlashcardReview review = spacedRepetitionService.processReview(testCard, testUser, 4);

        assertThat(review.getScheduledIntervalDays()).isEqualTo(7);
        assertThat(review.getStability()).isEqualTo(4.0);
        assertThat(review.getDifficulty()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("Subsequent review with 'Good' expands interval based on prior stability")
    void testSubsequentReviewGoodExpandsInterval() {
        FlashcardReview priorReview = FlashcardReview.builder()
            .flashcard(testCard)
            .user(testUser)
            .rating(3)
            .scheduledIntervalDays(3)
            .stability(2.5)
            .difficulty(5.0)
            .build();

        when(reviewRepository.findTopByFlashcardAndUserOrderByReviewedAtDesc(testCard, testUser))
            .thenReturn(Optional.of(priorReview));

        FlashcardReview review = spacedRepetitionService.processReview(testCard, testUser, 3);

        // interval = round(3 * 2.5) = 8
        assertThat(review.getScheduledIntervalDays()).isGreaterThan(3);
        assertThat(review.getStability()).isGreaterThan(2.5);
    }

    @Test
    @DisplayName("Subsequent lapse ('Again') resets interval to 1 day")
    void testSubsequentLapseResetsInterval() {
        FlashcardReview priorReview = FlashcardReview.builder()
            .flashcard(testCard)
            .user(testUser)
            .rating(4)
            .scheduledIntervalDays(14)
            .stability(5.0)
            .difficulty(3.5)
            .build();

        when(reviewRepository.findTopByFlashcardAndUserOrderByReviewedAtDesc(testCard, testUser))
            .thenReturn(Optional.of(priorReview));

        FlashcardReview review = spacedRepetitionService.processReview(testCard, testUser, 1);

        assertThat(review.getScheduledIntervalDays()).isEqualTo(1);
        assertThat(review.getDifficulty()).isGreaterThan(3.5);
    }
}

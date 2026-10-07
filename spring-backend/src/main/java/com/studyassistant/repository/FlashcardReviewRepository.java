package com.studyassistant.repository;

import com.studyassistant.entity.Flashcard;
import com.studyassistant.entity.FlashcardReview;
import com.studyassistant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlashcardReviewRepository extends JpaRepository<FlashcardReview, UUID> {

    Optional<FlashcardReview> findTopByFlashcardAndUserOrderByReviewedAtDesc(Flashcard flashcard, User user);

    List<FlashcardReview> findByUserOrderByReviewedAtDesc(User user);

    @Query("SELECT r FROM FlashcardReview r WHERE r.user.id = :userId AND r.nextReviewDate <= :cutoff")
    List<FlashcardReview> findDueReviews(@Param("userId") UUID userId, @Param("cutoff") Instant cutoff);

    @Query("""
        SELECT f FROM Flashcard f
        WHERE f.deck.course.id = :courseId
        AND (
            NOT EXISTS (
                SELECT 1 FROM FlashcardReview r WHERE r.flashcard = f AND r.user.id = :userId
            )
            OR (
                SELECT r2.nextReviewDate FROM FlashcardReview r2
                WHERE r2.flashcard = f AND r2.user.id = :userId
                ORDER BY r2.reviewedAt DESC
                LIMIT 1
            ) <= :cutoff
        )
    """)
    List<Flashcard> findDueFlashcardsByCourse(
        @Param("userId") UUID userId,
        @Param("courseId") UUID courseId,
        @Param("cutoff") Instant cutoff
    );
}

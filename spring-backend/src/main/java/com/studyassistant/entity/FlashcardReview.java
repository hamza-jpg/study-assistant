package com.studyassistant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "flashcard_reviews", indexes = {
    @Index(name = "idx_flashcard_reviews_due", columnList = "user_id, next_review_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlashcardReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flashcard_id", nullable = false)
    private Flashcard flashcard;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer rating; // 1: Again, 2: Hard, 3: Good, 4: Easy

    @Column(name = "scheduled_interval_days", nullable = false)
    @Builder.Default
    private Integer scheduledIntervalDays = 1;

    @Column(nullable = false)
    @Builder.Default
    private Double stability = 1.0;

    @Column(nullable = false)
    @Builder.Default
    private Double difficulty = 5.0;

    @Column(name = "next_review_date", nullable = false)
    private Instant nextReviewDate;

    @CreationTimestamp
    @Column(name = "reviewed_at", nullable = false, updatable = false)
    private Instant reviewedAt;
}

package com.studyassistant.dto.flashcard;

import com.studyassistant.entity.FlashcardDeck;
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
public class FlashcardDeckDto {
    private UUID id;
    private UUID courseId;
    private String courseTitle;
    private UUID userId;
    private String title;
    private String topic;
    private long cardCount;
    private Instant createdAt;

    public static FlashcardDeckDto fromEntity(FlashcardDeck deck, long cardCount) {
        return FlashcardDeckDto.builder()
            .id(deck.getId())
            .courseId(deck.getCourse().getId())
            .courseTitle(deck.getCourse().getTitle())
            .userId(deck.getUser() != null ? deck.getUser().getId() : null)
            .title(deck.getTitle())
            .topic(deck.getTopic())
            .cardCount(cardCount)
            .createdAt(deck.getCreatedAt())
            .build();
    }
}

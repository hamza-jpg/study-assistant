package com.studyassistant.dto.flashcard;

import com.studyassistant.entity.Flashcard;
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
public class FlashcardDto {
    private UUID id;
    private UUID deckId;
    private String question;
    private String answer;
    private String category;
    private String difficulty;
    private String sourceCitation;
    private Instant createdAt;

    public static FlashcardDto fromEntity(Flashcard card) {
        return FlashcardDto.builder()
            .id(card.getId())
            .deckId(card.getDeck().getId())
            .question(card.getQuestion())
            .answer(card.getAnswer())
            .category(card.getCategory())
            .difficulty(card.getDifficulty())
            .sourceCitation(card.getSourceCitation())
            .createdAt(card.getCreatedAt())
            .build();
    }
}

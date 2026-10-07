package com.studyassistant.controller;

import com.studyassistant.dto.flashcard.*;
import com.studyassistant.entity.User;
import com.studyassistant.service.AuthService;
import com.studyassistant.service.FlashcardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FlashcardController {

    private final FlashcardService flashcardService;
    private final AuthService authService;

    @GetMapping("/courses/{courseId}/flashcards/decks")
    public ResponseEntity<List<FlashcardDeckDto>> getDecks(@PathVariable UUID courseId) {
        return ResponseEntity.ok(flashcardService.getDecksByCourse(courseId));
    }

    @GetMapping("/flashcards/decks/{deckId}/cards")
    public ResponseEntity<List<FlashcardDto>> getDeckCards(@PathVariable UUID deckId) {
        return ResponseEntity.ok(flashcardService.getCardsByDeck(deckId));
    }

    @GetMapping("/courses/{courseId}/flashcards/due")
    public ResponseEntity<List<FlashcardDto>> getDueCards(@PathVariable UUID courseId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(flashcardService.getDueCards(courseId, user));
    }

    @PostMapping("/flashcards/{cardId}/review")
    public ResponseEntity<FlashcardReviewDto> reviewCard(
        @PathVariable UUID cardId,
        @Valid @RequestBody ReviewFlashcardRequest request
    ) {
        User user = authService.getCurrentAuthenticatedUser();
        FlashcardReviewDto review = flashcardService.recordReview(cardId, request.getRating(), user);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/courses/{courseId}/flashcards/generate")
    public ResponseEntity<FlashcardDeckDto> generateFlashcards(
        @PathVariable UUID courseId,
        @Valid @RequestBody GenerateFlashcardsRequest request
    ) {
        User user = authService.getCurrentAuthenticatedUser();
        request.setCourseId(courseId);
        FlashcardDeckDto deck = flashcardService.generateFlashcards(request, user);
        return ResponseEntity.ok(deck);
    }
}

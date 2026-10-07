package com.studyassistant.service;

import com.studyassistant.dto.flashcard.FlashcardDeckDto;
import com.studyassistant.dto.flashcard.FlashcardDto;
import com.studyassistant.dto.flashcard.FlashcardReviewDto;
import com.studyassistant.dto.flashcard.GenerateFlashcardsRequest;
import com.studyassistant.dto.rag.InternalFlashcardsRequest;
import com.studyassistant.dto.rag.InternalFlashcardsResponse;
import com.studyassistant.entity.Course;
import com.studyassistant.entity.Flashcard;
import com.studyassistant.entity.FlashcardDeck;
import com.studyassistant.entity.FlashcardReview;
import com.studyassistant.entity.User;
import com.studyassistant.repository.FlashcardDeckRepository;
import com.studyassistant.repository.FlashcardRepository;
import com.studyassistant.repository.FlashcardReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlashcardService {

    private final FlashcardDeckRepository deckRepository;
    private final FlashcardRepository flashcardRepository;
    private final FlashcardReviewRepository reviewRepository;
    private final SpacedRepetitionService spacedRepetitionService;
    private final CourseService courseService;
    private final PythonRagClient pythonRagClient;

    @Transactional(readOnly = true)
    public List<FlashcardDeckDto> getDecksByCourse(UUID courseId) {
        Course course = courseService.getCourseEntity(courseId);
        List<FlashcardDeck> decks = deckRepository.findByCourse(course);
        return decks.stream()
            .map(deck -> FlashcardDeckDto.fromEntity(deck, flashcardRepository.countByDeck(deck)))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<FlashcardDto> getCardsByDeck(UUID deckId) {
        FlashcardDeck deck = deckRepository.findById(deckId)
            .orElseThrow(() -> new IllegalArgumentException("Deck not found: " + deckId));
        return flashcardRepository.findByDeck(deck).stream()
            .map(FlashcardDto::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<FlashcardDto> getDueCards(UUID courseId, User user) {
        List<Flashcard> dueCards = reviewRepository.findDueFlashcardsByCourse(user.getId(), courseId, Instant.now());
        return dueCards.stream()
            .map(FlashcardDto::fromEntity)
            .toList();
    }

    @Transactional
    public FlashcardReviewDto recordReview(UUID flashcardId, Integer rating, User user) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
            .orElseThrow(() -> new IllegalArgumentException("Flashcard not found: " + flashcardId));

        FlashcardReview review = spacedRepetitionService.processReview(flashcard, user, rating);
        return FlashcardReviewDto.fromEntity(review);
    }

    @Transactional
    public FlashcardDeckDto generateFlashcards(GenerateFlashcardsRequest request, User user) {
        Course course = courseService.getCourseEntity(request.getCourseId());

        InternalFlashcardsRequest ragReq = InternalFlashcardsRequest.builder()
            .courseId(course.getId().toString())
            .topic(request.getTopic())
            .count(request.getCount())
            .build();

        InternalFlashcardsResponse ragResponse = pythonRagClient.generateFlashcards(ragReq).block();
        if (ragResponse == null || ragResponse.getFlashcards() == null || ragResponse.getFlashcards().isEmpty()) {
            throw new IllegalStateException("Failed to generate flashcards from course materials");
        }

        String deckTitle = request.getDeckTitle();
        if (deckTitle == null || deckTitle.isBlank()) {
            deckTitle = (request.getTopic() != null && !request.getTopic().isBlank())
                ? "Flashcards: " + request.getTopic()
                : course.getTitle() + " Core Concepts";
        }

        FlashcardDeck deck = FlashcardDeck.builder()
            .course(course)
            .user(user)
            .title(deckTitle)
            .topic(request.getTopic())
            .build();
        FlashcardDeck savedDeck = deckRepository.save(deck);

        List<Flashcard> createdCards = new ArrayList<>();
        for (Map<String, Object> cardMap : ragResponse.getFlashcards()) {
            String question = String.valueOf(cardMap.getOrDefault("question", ""));
            String answer = String.valueOf(cardMap.getOrDefault("answer", ""));
            String category = String.valueOf(cardMap.getOrDefault("category", "General"));
            String difficulty = String.valueOf(cardMap.getOrDefault("difficulty", "Medium"));
            String source = String.valueOf(cardMap.getOrDefault("source", "Course Materials"));

            if (!question.isBlank() && !answer.isBlank()) {
                Flashcard card = Flashcard.builder()
                    .deck(savedDeck)
                    .question(question)
                    .answer(answer)
                    .category(category)
                    .difficulty(difficulty)
                    .sourceCitation(source)
                    .build();
                createdCards.add(card);
            }
        }

        flashcardRepository.saveAll(createdCards);
        return FlashcardDeckDto.fromEntity(savedDeck, createdCards.size());
    }
}

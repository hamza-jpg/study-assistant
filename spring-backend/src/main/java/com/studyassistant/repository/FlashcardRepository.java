package com.studyassistant.repository;

import com.studyassistant.entity.Flashcard;
import com.studyassistant.entity.FlashcardDeck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, UUID> {
    List<Flashcard> findByDeck(FlashcardDeck deck);
    List<Flashcard> findByDeckId(UUID deckId);

    @Query("SELECT f FROM Flashcard f WHERE f.deck.course.id = :courseId")
    List<Flashcard> findByCourseId(@Param("courseId") UUID courseId);

    long countByDeck(FlashcardDeck deck);
}

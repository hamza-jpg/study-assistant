package com.studyassistant.repository;

import com.studyassistant.entity.Course;
import com.studyassistant.entity.FlashcardDeck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlashcardDeckRepository extends JpaRepository<FlashcardDeck, UUID> {
    List<FlashcardDeck> findByCourse(Course course);
}

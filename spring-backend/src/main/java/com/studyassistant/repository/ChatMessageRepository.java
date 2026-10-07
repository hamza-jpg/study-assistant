package com.studyassistant.repository;

import com.studyassistant.entity.ChatMessage;
import com.studyassistant.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    List<ChatMessage> findBySessionOrderByCreatedAtAsc(ChatSession session);
}

package com.studyassistant.repository;

import com.studyassistant.entity.ChatSession;
import com.studyassistant.entity.Course;
import com.studyassistant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {
    List<ChatSession> findByUserAndCourseOrderByUpdatedAtDesc(User user, Course course);
    List<ChatSession> findByUserOrderByUpdatedAtDesc(User user);
}

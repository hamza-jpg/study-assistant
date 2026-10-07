package com.studyassistant.repository;

import com.studyassistant.entity.Course;
import com.studyassistant.entity.Enrollment;
import com.studyassistant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    List<Enrollment> findByUser(User user);
    List<Enrollment> findByCourse(Course course);
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
    boolean existsByUserAndCourse(User user, Course course);
}

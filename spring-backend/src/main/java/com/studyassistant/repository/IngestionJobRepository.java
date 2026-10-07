package com.studyassistant.repository;

import com.studyassistant.entity.Course;
import com.studyassistant.entity.IngestionJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IngestionJobRepository extends JpaRepository<IngestionJob, UUID> {
    List<IngestionJob> findByCourseOrderByCreatedAtDesc(Course course);
}

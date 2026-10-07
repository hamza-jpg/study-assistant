package com.studyassistant.service;

import com.studyassistant.dto.course.CourseDto;
import com.studyassistant.dto.course.CreateCourseRequest;
import com.studyassistant.entity.Course;
import com.studyassistant.entity.Enrollment;
import com.studyassistant.entity.User;
import com.studyassistant.repository.CourseRepository;
import com.studyassistant.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional(readOnly = true)
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
            .map(CourseDto::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public CourseDto getCourseById(UUID courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        return CourseDto.fromEntity(course);
    }

    @Transactional(readOnly = true)
    public Course getCourseEntity(UUID courseId) {
        return courseRepository.findById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
    }

    @Transactional
    public CourseDto createCourse(CreateCourseRequest request, User instructor) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Course code already exists: " + request.getCode());
        }

        Course course = Course.builder()
            .code(request.getCode().toUpperCase().trim())
            .title(request.getTitle().trim())
            .description(request.getDescription())
            .instructor(instructor)
            .build();

        Course saved = courseRepository.save(course);

        // Auto-enroll the creator
        Enrollment enrollment = Enrollment.builder()
            .course(saved)
            .user(instructor)
            .build();
        enrollmentRepository.save(enrollment);

        return CourseDto.fromEntity(saved);
    }

    @Transactional
    public void enrollUser(UUID courseId, User user) {
        Course course = getCourseEntity(courseId);
        if (!enrollmentRepository.existsByUserAndCourse(user, course)) {
            Enrollment enrollment = Enrollment.builder()
                .course(course)
                .user(user)
                .build();
            enrollmentRepository.save(enrollment);
        }
    }

    @Transactional(readOnly = true)
    public List<CourseDto> getEnrolledCourses(User user) {
        return enrollmentRepository.findByUser(user).stream()
            .map(enrollment -> CourseDto.fromEntity(enrollment.getCourse()))
            .toList();
    }
}

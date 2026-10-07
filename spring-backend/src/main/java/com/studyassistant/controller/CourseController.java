package com.studyassistant.controller;

import com.studyassistant.dto.course.CourseDto;
import com.studyassistant.dto.course.CreateCourseRequest;
import com.studyassistant.entity.User;
import com.studyassistant.service.AuthService;
import com.studyassistant.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        CourseDto course = courseService.createCourse(request, user);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable UUID id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<Map<String, String>> enrollCourse(@PathVariable UUID id) {
        User user = authService.getCurrentAuthenticatedUser();
        courseService.enrollUser(id, user);
        return ResponseEntity.ok(Map.of("message", "Enrolled successfully", "courseId", id.toString()));
    }

    @GetMapping("/enrolled")
    public ResponseEntity<List<CourseDto>> getEnrolledCourses() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(courseService.getEnrolledCourses(user));
    }
}

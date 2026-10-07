package com.studyassistant.dto.course;

import com.studyassistant.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private UUID id;
    private String code;
    private String title;
    private String description;
    private UUID instructorId;
    private String instructorName;
    private Instant createdAt;

    public static CourseDto fromEntity(Course course) {
        return CourseDto.builder()
            .id(course.getId())
            .code(course.getCode())
            .title(course.getTitle())
            .description(course.getDescription())
            .instructorId(course.getInstructor() != null ? course.getInstructor().getId() : null)
            .instructorName(course.getInstructor() != null ? course.getInstructor().getFullName() : null)
            .createdAt(course.getCreatedAt())
            .build();
    }
}

package com.studyassistant.dto.course;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {
    @NotBlank(message = "Course code is required (e.g., CS101)")
    private String code;

    @NotBlank(message = "Course title is required")
    private String title;

    private String description;
}

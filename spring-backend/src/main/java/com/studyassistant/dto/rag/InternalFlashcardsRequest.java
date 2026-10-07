package com.studyassistant.dto.rag;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternalFlashcardsRequest {
    @JsonProperty("course_id")
    private String courseId;

    private String topic;

    @Builder.Default
    private Integer count = 5;
}

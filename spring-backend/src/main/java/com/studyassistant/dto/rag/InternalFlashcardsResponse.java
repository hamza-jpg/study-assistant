package com.studyassistant.dto.rag;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternalFlashcardsResponse {
    @JsonProperty("course_id")
    private String courseId;

    private List<Map<String, Object>> flashcards;

    private Integer total;

    private Boolean fallback;
}

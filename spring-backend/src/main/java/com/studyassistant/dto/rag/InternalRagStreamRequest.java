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
public class InternalRagStreamRequest {
    private String query;

    @JsonProperty("course_id")
    private String courseId;

    private List<Map<String, String>> history;

    @JsonProperty("top_k")
    @Builder.Default
    private Integer topK = 15;

    @JsonProperty("top_n")
    @Builder.Default
    private Integer topN = 5;

    @Builder.Default
    private Boolean augment = true;

    @JsonProperty("augment_mode")
    @Builder.Default
    private String augmentMode = "expand";

    @JsonProperty("use_web")
    @Builder.Default
    private Boolean useWeb = false;

    @JsonProperty("fallback_to_web")
    @Builder.Default
    private Boolean fallbackToWeb = true;
}

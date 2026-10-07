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
public class InternalIngestResponse {
    private Boolean success;

    @JsonProperty("course_id")
    private String courseId;

    @JsonProperty("document_id")
    private String documentId;

    @JsonProperty("job_id")
    private String jobId;

    @JsonProperty("chunks_created")
    private Integer chunksCreated;

    @JsonProperty("total_course_chunks")
    private Integer totalCourseChunks;
}

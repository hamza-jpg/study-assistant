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
public class InternalIngestRequest {
    @JsonProperty("course_id")
    private String courseId;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("document_id")
    private String documentId;

    @JsonProperty("job_id")
    private String jobId;
}

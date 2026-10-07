package com.studyassistant.dto.document;

import com.studyassistant.entity.IngestionJob;
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
public class IngestionJobDto {
    private UUID id;
    private UUID courseId;
    private UUID documentId;
    private String status;
    private Integer progress;
    private String errorMessage;
    private Instant createdAt;
    private Instant updatedAt;

    public static IngestionJobDto fromEntity(IngestionJob job) {
        return IngestionJobDto.builder()
            .id(job.getId())
            .courseId(job.getCourse().getId())
            .documentId(job.getDocument() != null ? job.getDocument().getId() : null)
            .status(job.getStatus())
            .progress(job.getProgress())
            .errorMessage(job.getErrorMessage())
            .createdAt(job.getCreatedAt())
            .updatedAt(job.getUpdatedAt())
            .build();
    }
}

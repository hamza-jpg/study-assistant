package com.studyassistant.dto.document;

import com.studyassistant.entity.Document;
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
public class DocumentDto {
    private UUID id;
    private UUID courseId;
    private String fileName;
    private String filePath;
    private Long fileSizeBytes;
    private Integer pageCount;
    private Integer chunkCount;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    public static DocumentDto fromEntity(Document doc) {
        return DocumentDto.builder()
            .id(doc.getId())
            .courseId(doc.getCourse().getId())
            .fileName(doc.getFileName())
            .filePath(doc.getFilePath())
            .fileSizeBytes(doc.getFileSizeBytes())
            .pageCount(doc.getPageCount())
            .chunkCount(doc.getChunkCount())
            .status(doc.getStatus())
            .createdAt(doc.getCreatedAt())
            .updatedAt(doc.getUpdatedAt())
            .build();
    }
}

package com.studyassistant.controller;

import com.studyassistant.dto.document.DocumentDto;
import com.studyassistant.dto.document.IngestionJobDto;
import com.studyassistant.dto.flashcard.FlashcardDto;
import com.studyassistant.service.DocumentService;
import com.studyassistant.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LegacyCompatController {

    private final DocumentService documentService;
    private final FlashcardService flashcardService;

    public static final UUID DEFAULT_COURSE_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        return ResponseEntity.ok(Map.of(
            "status", "ok",
            "service", "Study Assistant Enterprise Backend (Spring Boot 3 + Java 21)",
            "version", "2.0.0-enterprise",
            "database", "PostgreSQL 16 + pgvector",
            "virtual_threads", true,
            "timestamp", Instant.now()
        ));
    }

    @GetMapping("/documents")
    public ResponseEntity<List<DocumentDto>> getLegacyDocuments() {
        return ResponseEntity.ok(documentService.getDocumentsByCourse(DEFAULT_COURSE_ID));
    }

    @GetMapping("/ingest/status/{jobId}")
    public ResponseEntity<IngestionJobDto> getLegacyJobStatus(@PathVariable UUID jobId) {
        return ResponseEntity.ok(documentService.getJobStatus(jobId));
    }
}

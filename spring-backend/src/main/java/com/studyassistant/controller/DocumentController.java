package com.studyassistant.controller;

import com.studyassistant.dto.document.DocumentDto;
import com.studyassistant.dto.document.IngestionJobDto;
import com.studyassistant.entity.User;
import com.studyassistant.service.AuthService;
import com.studyassistant.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final AuthService authService;

    @PostMapping(value = "/courses/{courseId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<IngestionJobDto> uploadDocument(
        @PathVariable UUID courseId,
        @RequestParam("file") MultipartFile file
    ) throws IOException {
        User user = authService.getCurrentAuthenticatedUser();
        IngestionJobDto job = documentService.uploadAndEnqueue(courseId, file, user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(job);
    }

    @GetMapping("/courses/{courseId}/documents")
    public ResponseEntity<List<DocumentDto>> getDocuments(@PathVariable UUID courseId) {
        return ResponseEntity.ok(documentService.getDocumentsByCourse(courseId));
    }

    @GetMapping("/ingest/jobs/{jobId}")
    public ResponseEntity<IngestionJobDto> getJobStatus(@PathVariable UUID jobId) {
        return ResponseEntity.ok(documentService.getJobStatus(jobId));
    }
}

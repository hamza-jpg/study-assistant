package com.studyassistant.service;

import com.studyassistant.dto.document.DocumentDto;
import com.studyassistant.dto.document.IngestionJobDto;
import com.studyassistant.dto.rag.InternalIngestRequest;
import com.studyassistant.dto.rag.InternalIngestResponse;
import com.studyassistant.entity.Course;
import com.studyassistant.entity.Document;
import com.studyassistant.entity.IngestionJob;
import com.studyassistant.entity.User;
import com.studyassistant.repository.DocumentRepository;
import com.studyassistant.repository.IngestionJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final IngestionJobRepository ingestionJobRepository;
    private final CourseService courseService;
    private final PythonRagClient pythonRagClient;

    private static final Path UPLOADS_DIR = Paths.get("uploads").toAbsolutePath().normalize();

    @Transactional
    public IngestionJobDto uploadAndEnqueue(UUID courseId, MultipartFile file, User user) throws IOException {
        Course course = courseService.getCourseEntity(courseId);

        // Ensure upload directory exists
        Path courseUploadDir = UPLOADS_DIR.resolve(courseId.toString());
        Files.createDirectories(courseUploadDir);

        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document.pdf";
        String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
        Path targetPath = courseUploadDir.resolve(storedFileName);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Save Document Entity
        Document doc = Document.builder()
            .course(course)
            .fileName(originalFilename)
            .filePath(targetPath.toString())
            .fileSizeBytes(file.getSize())
            .status("PENDING")
            .build();
        Document savedDoc = documentRepository.save(doc);

        // Save IngestionJob Entity
        IngestionJob job = IngestionJob.builder()
            .course(course)
            .document(savedDoc)
            .status("PENDING")
            .progress(0)
            .build();
        IngestionJob savedJob = ingestionJobRepository.save(job);

        // Dispatch background processing using Java 21 Virtual Threads
        final UUID jobId = savedJob.getId();
        final UUID documentId = savedDoc.getId();
        final String absolutePath = targetPath.toString();

        Thread.startVirtualThread(() -> processIngestionAsync(jobId, documentId, courseId, absolutePath));

        return IngestionJobDto.fromEntity(savedJob);
    }

    private void processIngestionAsync(UUID jobId, UUID documentId, UUID courseId, String filePath) {
        log.info("Starting async background ingestion for job {} on virtual thread", jobId);
        try {
            // Update to PROCESSING
            updateJobStatus(jobId, "PROCESSING", 25, null);

            InternalIngestRequest req = InternalIngestRequest.builder()
                .courseId(courseId.toString())
                .filePath(filePath)
                .documentId(documentId.toString())
                .jobId(jobId.toString())
                .build();

            InternalIngestResponse response = pythonRagClient.ingestDocument(req).block();

            if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
                updateDocumentStatus(documentId, "READY", response.getChunksCreated());
                updateJobStatus(jobId, "COMPLETED", 100, null);
                log.info("Ingestion job {} succeeded with {} chunks", jobId, response.getChunksCreated());
            } else {
                String error = "Python ingestion failed or returned unsuccessful status";
                updateDocumentStatus(documentId, "FAILED", 0);
                updateJobStatus(jobId, "FAILED", 100, error);
            }
        } catch (Exception e) {
            log.error("Ingestion job {} failed with error: {}", jobId, e.getMessage(), e);
            updateDocumentStatus(documentId, "FAILED", 0);
            updateJobStatus(jobId, "FAILED", 100, e.getMessage());
        }
    }

    @Transactional
    public void updateJobStatus(UUID jobId, String status, int progress, String errorMessage) {
        ingestionJobRepository.findById(jobId).ifPresent(j -> {
            j.setStatus(status);
            j.setProgress(progress);
            j.setErrorMessage(errorMessage);
            ingestionJobRepository.save(j);
        });
    }

    @Transactional
    public void updateDocumentStatus(UUID documentId, String status, int chunkCount) {
        documentRepository.findById(documentId).ifPresent(d -> {
            d.setStatus(status);
            if (chunkCount > 0) {
                d.setChunkCount(chunkCount);
            }
            documentRepository.save(d);
        });
    }

    @Transactional(readOnly = true)
    public List<DocumentDto> getDocumentsByCourse(UUID courseId) {
        Course course = courseService.getCourseEntity(courseId);
        return documentRepository.findByCourse(course).stream()
            .map(DocumentDto::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public IngestionJobDto getJobStatus(UUID jobId) {
        IngestionJob job = ingestionJobRepository.findById(jobId)
            .orElseThrow(() -> new IllegalArgumentException("Ingestion job not found: " + jobId));
        return IngestionJobDto.fromEntity(job);
    }
}

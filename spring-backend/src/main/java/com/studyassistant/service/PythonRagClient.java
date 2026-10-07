package com.studyassistant.service;

import com.studyassistant.dto.rag.InternalFlashcardsRequest;
import com.studyassistant.dto.rag.InternalFlashcardsResponse;
import com.studyassistant.dto.rag.InternalIngestRequest;
import com.studyassistant.dto.rag.InternalIngestResponse;
import com.studyassistant.dto.rag.InternalRagStreamRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class PythonRagClient {

    private final WebClient pythonRagWebClient;

    public Flux<ServerSentEvent<String>> streamRag(InternalRagStreamRequest request) {
        ParameterizedTypeReference<ServerSentEvent<String>> typeRef = new ParameterizedTypeReference<>() {};

        return pythonRagWebClient.post()
            .uri("/internal/rag/stream")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.TEXT_EVENT_STREAM)
            .bodyValue(request)
            .retrieve()
            .bodyToFlux(typeRef)
            .timeout(Duration.ofSeconds(60))
            .doOnError(e -> log.error("Error communicating with Python RAG microservice stream: {}", e.getMessage()));
    }

    public Mono<InternalIngestResponse> ingestDocument(InternalIngestRequest request) {
        return pythonRagWebClient.post()
            .uri("/internal/ingest")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(InternalIngestResponse.class)
            .timeout(Duration.ofSeconds(120))
            .doOnError(e -> log.error("Error communicating with Python RAG microservice ingest: {}", e.getMessage()));
    }

    public Mono<InternalFlashcardsResponse> generateFlashcards(InternalFlashcardsRequest request) {
        return pythonRagWebClient.post()
            .uri("/internal/flashcards/generate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(InternalFlashcardsResponse.class)
            .timeout(Duration.ofSeconds(60))
            .doOnError(e -> log.error("Error communicating with Python RAG microservice flashcards: {}", e.getMessage()));
    }
}

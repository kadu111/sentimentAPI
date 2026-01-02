package com.hackaton_one.sentiment_api.api.controller;

import com.hackaton_one.sentiment_api.api.dto.BatchSentimentResponseDTO;
import com.hackaton_one.sentiment_api.api.dto.SentimentRequestDTO;
import com.hackaton_one.sentiment_api.api.dto.SentimentResponseDTO;
import com.hackaton_one.sentiment_api.service.BatchService;
import com.hackaton_one.sentiment_api.service.SentimentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * Controller principal da API de análise de sentimento.
 * 
 * Endpoints:
 * - POST /sentiment (texto único)
 * - POST /sentiment/batch (CSV em lote)
 */
@Slf4j
@RestController
@RequestMapping("/sentiment")
public class SentimentController {

    private final BatchService batchService;
    private final SentimentService sentimentService;

    public SentimentController(BatchService batchService, SentimentService sentimentService) {
        this.batchService = batchService;
        this.sentimentService = sentimentService;
    }

    /**
     * POST /sentiment - Análise de texto único.
     */
    @PostMapping
    public ResponseEntity<SentimentResponseDTO> analyzeSentiment(
            @Valid @RequestBody SentimentRequestDTO request) {

        var result = sentimentService.analyze(request.text());

        return ResponseEntity.ok(
                new SentimentResponseDTO(
                    result.previsao().toUpperCase(),
                    result.probabilidade(),
                    request.text()
                )
        );
    }

    /**
     * POST /sentiment/batch - Análise em lote via CSV.
     * 
     * @param file Arquivo CSV (obrigatório)
     * @param textColumn Nome da coluna com textos (opcional)
     */
    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BatchSentimentResponseDTO> analyzeBatchCSV(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "textColumn", required = false) String textColumn) {
        
        if (file.isEmpty()) {
            throw new IllegalArgumentException("CSV file is required");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("File must have .csv extension");
        }

        BatchSentimentResponseDTO response = batchService.processCSV(file, textColumn);

        if (response.totalProcessed() == 0) {
            throw new IllegalArgumentException("No valid text found in CSV");
        }

        return ResponseEntity.ok(response);
    }
}

package com.hackaton_one.sentiment_api.api.dto;

/**
 * Resposta de saída da análise de sentimento.
 *
 * Exemplo de resposta 200 OK:
 * {
 *   "sentiment": "POSITIVE",
 *   "score": 0.87,
 *   "text": "Este produto é muito bom!"
 * }
 */
public record SentimentResponseDTO(
        String sentiment,
        double score,
        String text
) {}


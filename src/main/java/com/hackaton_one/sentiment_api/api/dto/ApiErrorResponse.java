package com.hackaton_one.sentiment_api.api.dto;

import java.time.LocalDateTime;

/**
 * DTO padrão para respostas de erro da API.
 * Garante consistência no formato das mensagens retornadas ao cliente.
 */
public record ApiErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {}

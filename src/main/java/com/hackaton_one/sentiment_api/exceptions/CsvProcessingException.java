package com.hackaton_one.sentiment_api.exceptions;

/**
 * Exceção lançada quando ocorre erro no processamento de arquivos CSV.
 */
public class CsvProcessingException extends RuntimeException {

    public CsvProcessingException(String message) {
        super(message);
    }

    public CsvProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}


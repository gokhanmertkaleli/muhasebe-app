package com.muhasebe.exception;

/**
 * Geçersiz istek durumunda fırlatılır
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
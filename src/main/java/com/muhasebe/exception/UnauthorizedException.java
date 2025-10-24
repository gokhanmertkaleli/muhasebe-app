package com.muhasebe.exception;

/**
 * Yetkisiz erişim durumunda fırlatılır
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

// ===========================
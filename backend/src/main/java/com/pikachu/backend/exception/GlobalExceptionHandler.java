package com.pikachu.backend.exception;

// 전역 예외 핸들러

public class GlobalExceptionHandler extends RuntimeException {
    public GlobalExceptionHandler(String message) {
        super(message);
    }
}

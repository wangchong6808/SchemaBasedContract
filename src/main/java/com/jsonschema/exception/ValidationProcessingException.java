package com.jsonschema.exception;

public class ValidationProcessingException extends RuntimeException {

    public ValidationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
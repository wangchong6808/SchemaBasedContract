package com.jsonschema.exception;

public class FailedToLoadSchemaException extends RuntimeException {

    public FailedToLoadSchemaException(String message, Throwable cause) {
        super(message, cause);
    }
}

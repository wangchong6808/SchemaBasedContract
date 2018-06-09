package com.jsonschema.exception;

public class SchemaNotFoundException extends RuntimeException {

    public SchemaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

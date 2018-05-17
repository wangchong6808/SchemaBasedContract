package com.jsonschema.config;

public class SchemaNotFoundException extends RuntimeException {

    public SchemaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

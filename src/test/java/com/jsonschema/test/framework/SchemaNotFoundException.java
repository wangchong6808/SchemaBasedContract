package com.jsonschema.test.framework;

public class SchemaNotFoundException extends RuntimeException {

    public SchemaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

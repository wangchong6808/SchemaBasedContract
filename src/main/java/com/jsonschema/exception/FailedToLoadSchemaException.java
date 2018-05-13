package com.jsonschema.exception;

public class FailedToLoadSchemaException extends RuntimeException {

    private String schemaId;

    public FailedToLoadSchemaException(String message, Throwable cause, String schemaId) {
        super(message, cause);
        this.schemaId = schemaId;
    }

    public FailedToLoadSchemaException(String message, String schemaId) {
        super(message);
        this.schemaId = schemaId;
    }

    public String getSchemaId() {
        return schemaId;
    }
}

package com.jsonschema.exception;

import com.github.fge.jsonschema.core.report.ProcessingReport;

public class SchemaViolatedException extends RuntimeException {
    private ProcessingReport report;
    private String schema;

    public SchemaViolatedException() {
    }

    public SchemaViolatedException(String message) {
        super(message);
    }

    public SchemaViolatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchemaViolatedException(ProcessingReport report, String schema) {
        super("schema '"+ schema +"' violated.");
        this.report = report;
        this.schema = schema;
    }

    public ProcessingReport getReport() {
        return report;
    }

    public void setReport(ProcessingReport report) {
        this.report = report;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}

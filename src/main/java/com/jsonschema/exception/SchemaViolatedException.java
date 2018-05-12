package com.jsonschema.exception;

import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.jsonschema.validation.ValidationContext;

public class SchemaViolatedException extends RuntimeException {
    private ProcessingReport report;
    private ValidationContext context;

    public SchemaViolatedException(ProcessingReport report, ValidationContext context) {
        super("schema violated. : " + context.getSchemaId());
        this.report = report;
        this.context = context;
    }

    public ProcessingReport getReport() {
        return report;
    }

    public ValidationContext getContext() {
        return context;
    }
}

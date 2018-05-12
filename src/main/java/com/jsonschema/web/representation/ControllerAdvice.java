package com.jsonschema.web.representation;

import com.jsonschema.exception.FailedToLoadSchemaException;
import com.jsonschema.exception.SchemaViolatedException;
import com.jsonschema.validation.ValidationContext;
import com.jsonschema.web.error.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(SchemaViolatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleSchemaViolatedException(SchemaViolatedException e) {
        ValidationContext context = e.getContext();
        log.error("schema " + context.getSchemaId() + " in " + context.getTriggerPoint()+
                " violated. detailed report is " + e.getReport().toString(), e);
        return new ErrorResult("schema_violated", "schemaId:" + context.getSchemaId());
    }

}

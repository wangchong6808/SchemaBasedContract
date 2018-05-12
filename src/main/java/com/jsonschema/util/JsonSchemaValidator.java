package com.jsonschema.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ListProcessingReport;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.jsonschema.exception.SchemaViolatedException;
import com.jsonschema.exception.ValidationProcessingException;
import com.jsonschema.validation.ValidationContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonSchemaValidator {

    private static final String NAMESPACE_PREFIX = "resource:/schema/json/";

    private ClasspathSchemaLoader schemaLoader;

    public JsonSchemaValidator(ClasspathSchemaLoader schemaLoader) {
        this.schemaLoader = schemaLoader;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JodaModule());

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public void validateObject(ValidationContext context, Object object) {

        try {
            String jsonBody = objectMapper.writeValueAsString(object);

            JsonSchema schema = schemaLoader.getSchema(context.getSchemaId());
            ProcessingReport report = schema.validate(JsonLoader.fromString(jsonBody));
            context.setPayload(jsonBody);
            if (!report.isSuccess()) {
                throw new SchemaViolatedException(report, context);
            }
        } catch (ProcessingException | IOException e) {
            throw new ValidationProcessingException("unexpected error happened during schema validating. "
                    + e.getMessage(), e);
        }
    }

}

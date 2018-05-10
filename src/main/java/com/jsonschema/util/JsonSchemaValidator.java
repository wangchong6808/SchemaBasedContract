package com.jsonschema.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.load.uri.URITranslatorConfiguration;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.jsonschema.exception.SchemaViolatedException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

    public void validateObject(String schemaId, Object object) {

        try {
            String jsonBody = objectMapper.writeValueAsString(object);
            log.info("validate object {}", objectMapper.writeValueAsString(object));

            JsonSchema schema = schemaLoader.getSchema(schemaId);
            ProcessingReport report = schema.validate(JsonLoader.fromString(jsonBody));
            if (!report.isSuccess()) {
                throw new SchemaViolatedException(report, schemaId);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (ProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}

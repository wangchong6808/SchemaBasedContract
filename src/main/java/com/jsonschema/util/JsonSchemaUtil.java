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

@Slf4j
public class JsonSchemaUtil {

    private static final String NAMESPACE_PREFIX = "resource:/json/schema/";

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JodaModule());

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private SchemaLoader schemaLoader = new SchemaLoader();

    private static Map<String, JsonSchema> schemaMap = new HashMap<>();

    public static void validateObject(String ns, String schemaName, Object object) {

        try {
            String jsonBody = objectMapper.writeValueAsString(object);
            log.info("validate object {}", objectMapper.writeValueAsString(object));

            JsonSchema schema = getSchema(ns, schemaName);
            ProcessingReport report = schema.validate(JsonLoader.fromString(jsonBody));
            if (!report.isSuccess()) {
                throw new SchemaViolatedException(report, ns + schemaName);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (ProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static JsonSchema getSchema(String ns, String schemaName) throws ProcessingException {
        String namespace = NAMESPACE_PREFIX + ns;
        String schemaKey = namespace + schemaName;
        if (schemaMap.containsKey(schemaKey)) {
            return schemaMap.get(schemaKey);
        }

        URITranslatorConfiguration translatorCfg
                = URITranslatorConfiguration.newBuilder()
                .setNamespace(namespace).freeze();
        LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
                .setURITranslatorConfiguration(translatorCfg).freeze();

        JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
                .setLoadingConfiguration(cfg).freeze();
        JsonSchema schema = factory.getJsonSchema(schemaName+".json");
        schemaMap.put(schemaKey, schema);
        return schema;
    }

}

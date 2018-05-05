package com.jsonschema.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.load.uri.URITranslatorConfiguration;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.examples.Utils;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonSchemaUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .registerModule(new JodaModule());

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private SchemaLoader schemaLoader = new SchemaLoader();

    public static void validateObject(String ns, String schemaName, Object object) {

        try {
            String jsonBody = objectMapper.writeValueAsString(object);
            log.info("validate object {}", objectMapper.writeValueAsString(object));

            String namespace = "resource:/json/schema/" + ns;

            final URITranslatorConfiguration translatorCfg
                    = URITranslatorConfiguration.newBuilder()
                    .setNamespace(namespace).freeze();
            final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
                    .setURITranslatorConfiguration(translatorCfg).freeze();

            final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
                    .setLoadingConfiguration(cfg).freeze();

            final JsonSchema schema = factory.getJsonSchema(schemaName+".json");
            ProcessingReport report = schema.validate(JsonLoader.fromString(jsonBody));
            log.info("report: {}", report);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (ProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void validateJson(String ns, String schemaName, String object) {

    }

}

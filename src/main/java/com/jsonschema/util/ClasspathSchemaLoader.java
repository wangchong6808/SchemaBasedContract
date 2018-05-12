package com.jsonschema.util;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.load.uri.URITranslatorConfiguration;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.jsonschema.exception.FailedToLoadSchemaException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ClasspathSchemaLoader {

    private final Properties schemaConfig;

    private final Map<String, JsonSchema> schemaMap = new HashMap<>();

    private final JsonSchemaFactory factory;

    private final boolean cacheSchema;

    public ClasspathSchemaLoader(String basePath, Properties schemaConfig, boolean cacheSchema) {
        this.schemaConfig = schemaConfig;
        this.cacheSchema = cacheSchema;
        URITranslatorConfiguration translatorCfg
                = URITranslatorConfiguration.newBuilder()
                .setNamespace(basePath).freeze();
        LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
                .setURITranslatorConfiguration(translatorCfg).freeze();

        factory = JsonSchemaFactory.newBuilder()
                .setLoadingConfiguration(cfg).freeze();

    }

    public JsonSchema getSchema(String schemaId) {
        if (schemaMap.containsKey(schemaId)) {
            return schemaMap.get(schemaId);
        }

        try {
            JsonSchema schema = factory.getJsonSchema((String) schemaConfig.get(schemaId));
            if (cacheSchema) {
                schemaMap.put(schemaId, schema);
            }
            return schema;
        } catch (ProcessingException e) {
            throw new FailedToLoadSchemaException("failed to load schema " + schemaId, e);
        }
    }
}

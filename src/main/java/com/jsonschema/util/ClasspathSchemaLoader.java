package com.jsonschema.util;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.load.uri.URITranslatorConfiguration;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ClasspathSchemaLoader {

    private String basePath;
    private Properties schemaConfig;

    private Map<String, JsonSchema> schemaMap = new HashMap<>();

    public ClasspathSchemaLoader(String basePath, Properties schemaConfig) {
        this.basePath = basePath;
        this.schemaConfig = schemaConfig;
    }

    public JsonSchema getSchema(String schemaId) throws ProcessingException {
        String namespace = basePath + schemaConfig.get(schemaId);
        if (schemaMap.containsKey(schemaId)) {
            return schemaMap.get(schemaId);
        }

        URITranslatorConfiguration translatorCfg
                = URITranslatorConfiguration.newBuilder()
                .setNamespace(namespace).freeze();
        LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
                .setURITranslatorConfiguration(translatorCfg).freeze();

        JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
                .setLoadingConfiguration(cfg).freeze();
        JsonSchema schema = factory.getJsonSchema((String) schemaConfig.get(schemaId));
        schemaMap.put(schemaId, schema);
        return schema;
    }
}

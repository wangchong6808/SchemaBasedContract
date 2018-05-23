package com.jsonschema.config;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.load.uri.URITranslatorConfiguration;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SchemaConfiguration {

    private final String bastPath;

    private final Properties config;

    private final JsonSchemaFactory factory;

    private final Map<String, JsonSchema> schemaMap = new HashMap<>();

    public SchemaConfiguration(String bastPath, Properties config) {
        this.bastPath = bastPath;
        this.config = config;
        URITranslatorConfiguration translatorCfg
                = URITranslatorConfiguration.newBuilder()
                .setNamespace("resource:" + bastPath).freeze();
        LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
                .setURITranslatorConfiguration(translatorCfg).freeze();

        factory = JsonSchemaFactory.newBuilder()
                .setLoadingConfiguration(cfg).freeze();
    }

    public JsonSchema getSchema(String apiId) throws ProcessingException, IOException {
        if (!schemaMap.containsKey(apiId)) {
            JsonSchema schema = factory.getJsonSchema(JsonLoader.fromPath(bastPath + config.getProperty(apiId)));
            schemaMap.put(apiId, schema);
        }
        return schemaMap.get(apiId);
    }
}

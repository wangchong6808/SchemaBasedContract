package com.jsonschema.util;

import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.load.uri.URITranslatorConfiguration;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.util.Properties;

public abstract class SchemaLoader {

    protected final Properties schemaConfig;

    protected JsonSchemaFactory factory;

    protected final String basePath;


    public SchemaLoader(String basePath, Properties schemaConfig) {
        this.basePath = basePath;
        this.schemaConfig = schemaConfig;
        URITranslatorConfiguration translatorCfg
                = URITranslatorConfiguration.newBuilder()
                .setNamespace("resource:" + basePath).freeze();
        LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
                .setURITranslatorConfiguration(translatorCfg).freeze();

        factory = JsonSchemaFactory.newBuilder()
                .setLoadingConfiguration(cfg).freeze();
    }

    public abstract JsonSchema loadSchema(String schemaId) ;
}

package com.jsonschema.util;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.load.uri.URITranslatorConfiguration;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.jsonschema.exception.FailedToLoadSchemaException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ClasspathSchemaLoader {

    private final Properties schemaConfig;

    private  JsonSchemaFactory factory;

    private final String basePath;

    private boolean fromClasspath;


    public ClasspathSchemaLoader(String basePath, Properties schemaConfig) {
        this(basePath, schemaConfig, true);

    }

    public ClasspathSchemaLoader(String basePath, Properties schemaConfig, boolean fromClasspath) {
        this.basePath = basePath;
        this.schemaConfig = schemaConfig;
        this.fromClasspath = fromClasspath;
        URITranslatorConfiguration translatorCfg
                = URITranslatorConfiguration.newBuilder()
                .setNamespace("resource:" + basePath).freeze();
        LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
                .setURITranslatorConfiguration(translatorCfg).freeze();

        factory = JsonSchemaFactory.newBuilder()
                .setLoadingConfiguration(cfg).freeze();

    }

    public JsonSchema loadSchema(String schemaId) {
        String schemaFile = (String) schemaConfig.get(schemaId);
        if (schemaFile == null) {
            throw new FailedToLoadSchemaException("schema config not found : "+schemaId, schemaId);
        }

        try {
            if (fromClasspath) {
                return factory.getJsonSchema(schemaFile);
            } else {
                return factory.getJsonSchema(JsonLoader.fromPath(basePath + schemaConfig.getProperty(schemaId)));
            }
        } catch (ProcessingException e) {
            throw new FailedToLoadSchemaException("failed to load schema : " + schemaId, e, schemaId);
        } catch (IOException e) {
            throw new FailedToLoadSchemaException("read schema file error : "+schemaId, e, schemaId);
        }
    }
}

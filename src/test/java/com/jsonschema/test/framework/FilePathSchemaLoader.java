package com.jsonschema.test.framework;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.load.uri.URITranslatorConfiguration;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.jsonschema.exception.FailedToLoadSchemaException;
import com.jsonschema.util.SchemaLoader;

import java.io.IOException;
import java.util.Properties;

public class FilePathSchemaLoader extends SchemaLoader {


    public FilePathSchemaLoader(String basePath, Properties schemaConfig) {
        super(basePath, schemaConfig);
    }


    public JsonSchema loadSchema(String schemaId) {
        String schemaFile = (String) schemaConfig.get(schemaId);
        if (schemaFile == null) {
            throw new FailedToLoadSchemaException("schema config not found : "+schemaId, schemaId);
        }

        try {
            return factory.getJsonSchema(JsonLoader.fromPath(basePath + schemaConfig.getProperty(schemaId)));
        } catch (ProcessingException e) {
            throw new FailedToLoadSchemaException("failed to load schema : " + schemaId, e, schemaId);
        } catch (IOException e) {
            throw new FailedToLoadSchemaException("read schema file error : "+schemaId, e, schemaId);
        }
    }
}

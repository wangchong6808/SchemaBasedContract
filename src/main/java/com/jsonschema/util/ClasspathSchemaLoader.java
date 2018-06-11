package com.jsonschema.util;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.jsonschema.exception.FailedToLoadSchemaException;

import java.util.Properties;

public class ClasspathSchemaLoader extends SchemaLoader {

    public ClasspathSchemaLoader(String basePath, Properties schemaConfig) {
        super(basePath, schemaConfig);
    }

    public JsonSchema loadSchema(String schemaId) {
        String schemaFile = (String) schemaConfig.get(schemaId);
        if (schemaFile == null) {
            throw new FailedToLoadSchemaException("schema config not found : "+schemaId, schemaId);
        }

        try {
            return factory.getJsonSchema(schemaFile);
        } catch (ProcessingException e) {
            throw new FailedToLoadSchemaException("failed to load schema : " + schemaId, e, schemaId);
        }
    }
}

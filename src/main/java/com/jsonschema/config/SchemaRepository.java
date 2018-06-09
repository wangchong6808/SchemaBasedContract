package com.jsonschema.config;

import com.github.fge.jsonschema.main.JsonSchema;
import com.jsonschema.util.ClasspathSchemaLoader;

import java.util.HashMap;
import java.util.Map;

public class SchemaRepository {

    private boolean cacheSchema = true;

    private final Map<String, JsonSchema> schemaMap = new HashMap<>();

    protected ClasspathSchemaLoader schemaLoader;

    public JsonSchema findById(String schemaId) {
        if (!cacheSchema) {
            return schemaLoader.loadSchema(schemaId);
        }
        if (!schemaMap.containsKey(schemaId)) {
            synchronized (this) {
                if (!schemaMap.containsKey(schemaId)) {
                    JsonSchema schema = schemaLoader.loadSchema(schemaId);
                    schemaMap.put(schemaId, schema);
                }
            }
        }
        return schemaMap.get(schemaId);
    }

    public SchemaRepository(ClasspathSchemaLoader schemaLoader) {
        this.schemaLoader = schemaLoader;
    }

    public SchemaRepository(ClasspathSchemaLoader schemaLoader, boolean cacheSchema) {
        this.cacheSchema = cacheSchema;
        this.schemaLoader = schemaLoader;
    }
}

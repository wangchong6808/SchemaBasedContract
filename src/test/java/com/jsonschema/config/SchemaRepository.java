package com.jsonschema.config;

import com.github.fge.jsonschema.main.JsonSchema;

public interface SchemaRepository {

    JsonSchema findSchemaByServiceAndApiId(String service, String apiId);
}

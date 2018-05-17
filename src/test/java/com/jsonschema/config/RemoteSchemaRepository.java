package com.jsonschema.config;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RemoteSchemaRepository implements SchemaRepository {

    private Map<String, SchemaConfiguration> schemaConfigurationMap = new HashMap<>();

    @Override
    public JsonSchema findSchemaByServiceAndApiId(String service, String apiId) throws SchemaNotFoundException {
        try {
            if (!schemaConfigurationMap.containsKey(service)) {
                SchemaConfiguration configuration = SchemaLoader.load(service);
                schemaConfigurationMap.put(service, configuration);
            }
            return schemaConfigurationMap.get(service).getSchema(apiId);
        } catch (IOException e) {
            throw new SchemaNotFoundException("service:"+service+" apiId:"+apiId, e);
        } catch (ProcessingException e) {
            throw new SchemaNotFoundException("service:"+service+" apiId:"+apiId, e);
        }
    }
}

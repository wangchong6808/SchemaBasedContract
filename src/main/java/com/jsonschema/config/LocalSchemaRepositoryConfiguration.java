package com.jsonschema.config;

import com.jsonschema.util.ClasspathSchemaLoader;
import com.jsonschema.web.interceptor.ControllerSchemaValidationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class LocalSchemaRepositoryConfiguration {


    @Value("${validation.schema.devmode:false}")
    private boolean devMode;

    @Bean
    public SchemaRepository init() throws IOException {
        Properties properties = new Properties();
        properties.load(ControllerSchemaValidationInterceptor.class.getResourceAsStream("/schema/json/schema_config.properties"));
        ClasspathSchemaLoader schemaLoader = new ClasspathSchemaLoader("/schema/json/", properties);
        return new SchemaRepository(schemaLoader, !devMode);
    }
}

package com.jsonschema.web.interceptor;

import com.jsonschema.util.ClasspathSchemaLoader;
import com.jsonschema.util.JsonSchemaValidator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@Aspect
@Component
public class FeignClientSchemaValidationInterceptor {

    SchemaValidationInterceptorUtil schemaValidationInterceptorUtil;

    @AfterReturning(value = "target(com.jsonschema.web.client.SchemaAwareClient)", returning = "response")
    public void validateResponse(JoinPoint joinPoint, Object response) {
        schemaValidationInterceptorUtil.validateOutput(joinPoint, response);
    }

    @PostConstruct
    public void init() throws IOException {
        Properties properties = new Properties();
        properties.load(ControllerSchemaValidationInterceptor.class.getResourceAsStream("/schema/json/schema_config.properties"));

        ClasspathSchemaLoader schemaLoader = new ClasspathSchemaLoader("resource:/schema/json/", properties, true);
        schemaValidationInterceptorUtil = new SchemaValidationInterceptorUtil(new JsonSchemaValidator(schemaLoader));
    }
}

package com.jsonschema.web.interceptor;

import com.jsonschema.util.ClasspathSchemaLoader;
import com.jsonschema.util.JsonSchemaValidator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@Aspect
@Component
public class ControllerSchemaValidationInterceptor {

    @Value("${validation.schema.devmode:false}")
    private boolean devMode;

    SchemaValidationInterceptorHelper schemaValidationInterceptorUtil;


    @Before("@annotation(com.jsonschema.annotation.JsonSchema) " +
            "&& args(.., @org.springframework.web.bind.annotation.RequestBody body)")
    public void validateRequest(JoinPoint joinPoint, Object body) {
        schemaValidationInterceptorUtil.validateInput(joinPoint, body);
    }

    @AfterReturning(value = "@annotation(com.jsonschema.annotation.JsonSchema)", returning = "response")
    public void validateResponse(JoinPoint joinPoint, Object response) {
        schemaValidationInterceptorUtil.validateOutput(joinPoint, response);
    }

    @PostConstruct
    public void init() throws IOException {
        Properties properties = new Properties();
        properties.load(ControllerSchemaValidationInterceptor.class.getResourceAsStream("/schema/json/schema_config.properties"));

        ClasspathSchemaLoader schemaLoader = new ClasspathSchemaLoader("resource:/schema/json/", properties, devMode);
        schemaValidationInterceptorUtil = new SchemaValidationInterceptorHelper(new JsonSchemaValidator(schemaLoader));
    }
}

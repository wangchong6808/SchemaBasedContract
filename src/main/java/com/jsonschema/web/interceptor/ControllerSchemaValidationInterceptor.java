package com.jsonschema.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerSchemaValidationInterceptor {


    @Autowired
    private SchemaValidationInterceptorHelper schemaValidationInterceptorHelper;


    @Before("@annotation(com.jsonschema.annotation.JsonSchema) " +
            "&& args(.., @org.springframework.web.bind.annotation.RequestBody body)")
    public void validateRequest(JoinPoint joinPoint, Object body) {
        schemaValidationInterceptorHelper.validateInput(joinPoint, body);
    }

    @AfterReturning(value = "@annotation(com.jsonschema.annotation.JsonSchema)", returning = "response")
    public void validateResponse(JoinPoint joinPoint, Object response) {
        schemaValidationInterceptorHelper.validateOutput(joinPoint, response);
    }

}

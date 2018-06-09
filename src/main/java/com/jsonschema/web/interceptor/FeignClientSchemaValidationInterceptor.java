package com.jsonschema.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class FeignClientSchemaValidationInterceptor {

    @Autowired
    private SchemaValidationInterceptorHelper schemaValidationInterceptorHelper;

    @AfterReturning(value = "target(com.jsonschema.web.client.SchemaAwareClient)", returning = "response")
    public void validateResponse(JoinPoint joinPoint, Object response) {
        schemaValidationInterceptorHelper.validateOutput(joinPoint, response);
    }

}

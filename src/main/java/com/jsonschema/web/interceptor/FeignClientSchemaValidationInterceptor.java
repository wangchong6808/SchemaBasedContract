package com.jsonschema.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class FeignClientSchemaValidationInterceptor {

    /*@AfterReturning(value = "target(com.jsonschema.web.client.CustomerClient)", returning = "response")
    public void logControllerMethod(JoinPoint joinPoint, Object response) {

        SchemaValidationInterceptorUtil.validateOutput(joinPoint, response);
    }*/

    @AfterReturning(value = "target(com.jsonschema.web.client.SchemaAwareClient)", returning = "response")
    public void logControllerMethod(JoinPoint joinPoint, Object response) {

        //SchemaValidationInterceptorUtil.validateOutput(joinPoint, response);
    }
}

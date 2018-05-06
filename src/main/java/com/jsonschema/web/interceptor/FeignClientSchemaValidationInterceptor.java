package com.jsonschema.web.interceptor;

import com.jsonschema.annotation.JsonSchema;
import com.jsonschema.util.JsonSchemaUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

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

        SchemaValidationInterceptorUtil.validateOutput(joinPoint, response);
    }
}

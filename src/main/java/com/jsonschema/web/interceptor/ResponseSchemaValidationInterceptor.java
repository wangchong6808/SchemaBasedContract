package com.jsonschema.web.interceptor;

import com.jsonschema.util.JsonSchemaUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import com.jsonschema.annotation.JsonSchema;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ResponseSchemaValidationInterceptor {


    @Before("@annotation(com.jsonschema.annotation.JsonSchema) " +
            "&& args(.., @org.springframework.web.bind.annotation.RequestBody body)")
    public void validateRequest(JoinPoint joinPoint, Object body) {
        SchemaValidationInterceptorUtil.validateInput(joinPoint, body);
    }

    @AfterReturning(value = "@annotation(com.jsonschema.annotation.JsonSchema)", returning = "response")
    public void validateResponse(JoinPoint joinPoint, Object response) {
        SchemaValidationInterceptorUtil.validateOutput(joinPoint, response);
    }
}

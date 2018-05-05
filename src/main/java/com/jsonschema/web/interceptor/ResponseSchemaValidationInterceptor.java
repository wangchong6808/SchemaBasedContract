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
    public void logControllerMethod(JoinPoint joinPoint, Object body) {
        if (body == null) {
            return;
        }
        JsonSchema annotation = getSchemaAnnotation(joinPoint);
        String requestSchema = annotation.requestSchema();
        if (!StringUtils.hasText(requestSchema)) {
            return;
        }
        JsonSchemaUtil.validateObject(annotation.ns(), requestSchema, body);
    }

    @AfterReturning(value = "@within(org.springframework.web.bind.annotation.RestController)", returning = "response")
    public void validateResponse(JoinPoint joinPoint, Object response) {
        if (response == null) {
            return;
        }
        JsonSchema annotation = getSchemaAnnotation(joinPoint);
        String responseSchema = annotation.responseSchema();
        if (!StringUtils.hasText(responseSchema)) {
            return;
        }
        JsonSchemaUtil.validateObject(annotation.ns(), responseSchema, response);
        log.info("method: {}, args: {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    private static JsonSchema getSchemaAnnotation(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(JsonSchema.class);
    }
}

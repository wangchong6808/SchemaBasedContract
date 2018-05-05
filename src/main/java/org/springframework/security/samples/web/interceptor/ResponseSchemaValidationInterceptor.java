package org.springframework.security.samples.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.samples.schema.annotation.JsonSchema;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ResponseSchemaValidationInterceptor {


    @Before("@annotation(org.springframework.security.samples.schema.annotation.JsonSchema) " +
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
    }

    @AfterReturning(value = "@within(org.springframework.web.bind.annotation.RestController)", returning = "response")
    public void validateResponse(JoinPoint joinPoint, Object response) {
        if (response == null) {
            return;
        }

        log.info("method: {}, args: {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    private static JsonSchema getSchemaAnnotation(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(JsonSchema.class);
    }
}

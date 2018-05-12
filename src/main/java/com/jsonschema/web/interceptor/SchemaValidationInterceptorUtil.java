package com.jsonschema.web.interceptor;

import com.jsonschema.annotation.JsonSchema;
import com.jsonschema.exception.SchemaViolatedException;
import com.jsonschema.util.JsonSchemaValidator;
import com.jsonschema.validation.ValidationContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

@Slf4j
public class SchemaValidationInterceptorUtil {

    private JsonSchemaValidator jsonSchemaValidator;

    public SchemaValidationInterceptorUtil(JsonSchemaValidator jsonSchemaValidator) {
        this.jsonSchemaValidator = jsonSchemaValidator;
    }

    public void validateInput(JoinPoint joinPoint, Object content) {
        if (content == null) {
            return;
        }
        JsonSchema annotation = getSchemaAnnotation(joinPoint);
        String inputSchema = annotation.inputSchema();
        if (!StringUtils.hasText(inputSchema)) {
            return;
        }
        ValidationContext context = ValidationContext.builder()
                .triggerPoint(joinPoint.getTarget().getClass().getName())
                .schemaId(inputSchema).build();
        jsonSchemaValidator.validateObject(context, content);
    }

    public void validateOutput(JoinPoint joinPoint, Object content) {
        if (content == null) {
            return;
        }
        JsonSchema annotation = getSchemaAnnotation(joinPoint);
        String outputSchema = annotation.outputSchema();
        if (!StringUtils.hasText(outputSchema)) {
            return;
        }
        ValidationContext context = ValidationContext.builder()
                .triggerPoint(joinPoint.getTarget().getClass().getName())
                .schemaId(outputSchema).build();
        jsonSchemaValidator.validateObject(context, content);
    }

    private static JsonSchema getSchemaAnnotation(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(JsonSchema.class);
    }
}

package com.jsonschema.web.interceptor;

import com.jsonschema.annotation.JsonSchema;
import com.jsonschema.util.JsonSchemaUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

public class SchemaValidationInterceptorUtil {

    public static void validateInput(JoinPoint joinPoint, Object content) {
        if (content == null) {
            return;
        }
        JsonSchema annotation = getSchemaAnnotation(joinPoint);
        String inputSchema = annotation.inputSchema();
        if (!StringUtils.hasText(inputSchema)) {
            return;
        }
        JsonSchemaUtil.validateObject(annotation.ns(), inputSchema, content);
    }

    public static void validateOutput(JoinPoint joinPoint, Object content) {
        if (content == null) {
            return;
        }
        JsonSchema annotation = getSchemaAnnotation(joinPoint);
        String outputSchema = annotation.outputSchema();
        if (!StringUtils.hasText(outputSchema)) {
            return;
        }
        JsonSchemaUtil.validateObject(annotation.ns(), outputSchema, content);
    }

    private static JsonSchema getSchemaAnnotation(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(JsonSchema.class);
    }
}

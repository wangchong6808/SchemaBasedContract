package com.jsonschema.web.interceptor;

import com.jsonschema.annotation.JsonSchema;
import com.jsonschema.config.SchemaRepository;
import com.jsonschema.util.JsonSchemaValidator;
import com.jsonschema.validation.ValidationContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class SchemaValidationInterceptorHelper {

    private JsonSchemaValidator jsonSchemaValidator = new JsonSchemaValidator();

    @Autowired
    private SchemaRepository schemaRepository;

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
                .schemaId(inputSchema)
                .schema(schemaRepository.findById(inputSchema)).build();
        jsonSchemaValidator.validateObject(context, content);
    }

    public void validateOutput(JoinPoint joinPoint, Object content) {
        JsonSchema annotation = getSchemaAnnotation(joinPoint);
        String outputSchema = annotation.outputSchema();
        if (!StringUtils.hasText(outputSchema)) {
            return;
        }
        ValidationContext context = ValidationContext.builder()
                .triggerPoint(joinPoint.getTarget().getClass().getName())
                .schemaId(outputSchema)
                .schema(schemaRepository.findById(outputSchema)).build();
        jsonSchemaValidator.validateObject(context, content);
    }

    private static JsonSchema getSchemaAnnotation(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(JsonSchema.class);
    }
}

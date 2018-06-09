package com.jsonschema.aop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.jsonschema.annotation.JsonSchema;
import com.jsonschema.config.SchemaRepositoryLocator;
import com.jsonschema.exception.SchemaViolatedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cloud.netflix.feign.FeignClient;

import java.lang.reflect.Method;

@Slf4j
@Aspect
public class MyAspect {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private static final String PROXY_CALLBACK_METHOD_NAME = "getCallback";


    @AfterReturning(value = "target(com.jsonschema.web.client.SchemaAwareClient)", returning = "response")
    public void after(JoinPoint joinPoint, Object response) throws Throwable {
        if ("toString".equals(joinPoint.getSignature().getName()) || PROXY_CALLBACK_METHOD_NAME.equals(joinPoint.getSignature().getName())) {
            return;
        }
        if (joinPoint.getTarget().getClass().getName().startsWith("com.sun.proxy")) {
            return;
        }
        if (response == null) {
            return;
        }
        JsonSchema schema = retrieveMethodAnnotationFrom((ProceedingJoinPoint) joinPoint, JsonSchema.class);
        System.out.println("got you " + joinPoint.getSignature() + " " + schema);
        FeignClient feignClient = retrieveClassAnnotation((ProceedingJoinPoint) joinPoint, FeignClient.class);
        System.out.println("got you class " + feignClient);
        com.github.fge.jsonschema.main.JsonSchema jsonSchema = SchemaRepositoryLocator.locate(feignClient.name()).findById(schema.remoteSchema());
        ProcessingReport report = jsonSchema.validate(JsonLoader.fromString(objectMapper.writeValueAsString(response)));
        log.info("start to validate data by remote interface schema, service:{} ,remoteSchema:{}", feignClient.name(), schema.remoteSchema());
        if (report.isSuccess()) {
            log.info("remote interface schema validation successful");
        } else {
            log.error("validation failed, service:{}, remoteSchema:{}", feignClient.name(), schema.remoteSchema());
            log.error(report.toString());
            throw new SchemaViolatedException(report);
        }
    }

    private <T> T retrieveClassAnnotation(ProceedingJoinPoint joinPoint, Class annotationClass) throws ClassNotFoundException {
        String proxyTargetName = joinPoint.getTarget().getClass().getName();
        String className = joinPoint.getTarget().getClass().getName().substring(0, proxyTargetName.indexOf("$$"));
        Class targetClass = Class.forName(className);
        return (T) targetClass.getAnnotation(annotationClass);
    }

    private <T> T retrieveMethodAnnotationFrom(ProceedingJoinPoint joinPoint, Class annotationClass) throws Throwable {
        return (T) retrieveTargetMethodFrom(joinPoint).getAnnotation(annotationClass);
    }

    private Method retrieveTargetMethodFrom(ProceedingJoinPoint joinPoint) throws Throwable {
        String proxyTargetName = joinPoint.getTarget().getClass().getName();
        String className = joinPoint.getTarget().getClass().getName().substring(0, proxyTargetName.indexOf("$$"));
        Class targetClass = Class.forName(className);
        return targetClass.getMethod(joinPoint.getSignature().getName(), retrieveArgTypesFrom(joinPoint));
    }

    private Class[] retrieveArgTypesFrom(ProceedingJoinPoint joinPoint) {
        Class[] argTypes = new Class[joinPoint.getArgs().length];
        for (int i = 0; i < argTypes.length; i++) {
            argTypes[i] = joinPoint.getArgs()[i].getClass();
        }
        return argTypes;
    }
}

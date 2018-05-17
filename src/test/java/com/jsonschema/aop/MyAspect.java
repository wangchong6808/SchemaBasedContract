package com.jsonschema.aop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.jsonschema.annotation.JsonSchema;
import com.jsonschema.config.RemoteSchemaRepository;
import com.jsonschema.config.SchemaRepository;
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

    private SchemaRepository repository = new RemoteSchemaRepository();

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    @AfterReturning(value = "target(com.jsonschema.web.client.SchemaAwareClient)", returning = "response")
    public void after(JoinPoint joinPoint, Object response) throws Throwable {
        //Class<? extends Object> cls = joinPoint.getTarget().getClass();
        JsonSchema schema = retrieveMethodAnnotationFrom((ProceedingJoinPoint) joinPoint, JsonSchema.class);
        System.out.println("got you " + joinPoint.getSignature() + " " + schema);
        FeignClient feignClient = retrieveClassAnnotation((ProceedingJoinPoint) joinPoint, FeignClient.class);
        System.out.println("got you class " + feignClient);
        com.github.fge.jsonschema.main.JsonSchema jsonSchema = repository.findSchemaByServiceAndApiId(feignClient.name(), schema.remoteSchema());
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

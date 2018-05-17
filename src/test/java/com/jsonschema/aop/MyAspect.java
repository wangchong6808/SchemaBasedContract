package com.jsonschema.aop;

import com.jsonschema.annotation.JsonSchema;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cloud.netflix.feign.FeignClient;

import java.lang.reflect.Method;

@Aspect
public class MyAspect {


    @AfterReturning(value = "target(com.jsonschema.web.client.SchemaAwareClient)", returning = "response")
    public void after(JoinPoint joinPoint, Object response) throws Throwable {
        //Class<? extends Object> cls = joinPoint.getTarget().getClass();
        JsonSchema schema = retrieveMethodAnnotationFrom((ProceedingJoinPoint) joinPoint, JsonSchema.class);
        System.out.println("got you " + joinPoint.getSignature() + " " + schema);
        FeignClient feignClient = retrieveClassAnnotation((ProceedingJoinPoint) joinPoint, FeignClient.class);
        System.out.println("got you class " + feignClient);
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

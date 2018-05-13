package com.jsonschema.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MyAspect {




    @AfterReturning(value = "execution(* *(..))", returning = "response")
    public void after(JoinPoint joinPoint, Object response) {
        System.out.println("got you " + joinPoint.getSignature() + " " + response);
    }
}

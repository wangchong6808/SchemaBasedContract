package com.jsonschema.aop;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class SchemaDetector {

    public static <T> T detect(T o) {
        AspectJProxyFactory factory = new AspectJProxyFactory(o);
        MyAspect aspect = new MyAspect();
        factory.addAspect(aspect);
        return factory.getProxy();
    }
}

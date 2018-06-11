package com.jsonschema.test.framework.aop;

import org.mockito.Mockito;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class MockBeanUtil {

    public static Object mockBean(String className) throws ClassNotFoundException {
        return mockBean(Class.forName(className));
    }

    public static Object mockBean(Class cls) {
        Object mock = Mockito.mock(cls);
        AspectJProxyFactory factory = new AspectJProxyFactory(mock);
        ClientAspect aspect = new ClientAspect();
        factory.addAspect(aspect);
        return factory.getProxy();
    }
}

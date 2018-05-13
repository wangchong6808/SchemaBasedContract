package com.jsonschema.aop;

import com.jsonschema.web.client.CustomerClient;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class APOTest {


    @Test
    void test() {
        MyTarget target = new MyTarget();
        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        MyAspect aspect = new MyAspect();
        factory.addAspect(aspect);
        MyTarget proxy = factory.getProxy();
        proxy.getMessage();
    }
}

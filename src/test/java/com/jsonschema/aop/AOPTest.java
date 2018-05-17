package com.jsonschema.aop;

import com.jsonschema.web.client.CustomerClient;
import com.jsonschema.web.dto.Customer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class AOPTest {


    @Test
    void test() {
        MyTarget target = new MyTarget();
        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        MyAspect aspect = new MyAspect();
        factory.addAspect(aspect);
        MyTarget proxy = factory.getProxy();
        proxy.getMessage();
    }

    @Test
    void testClient() {
        CustomerClient client = Mockito.mock(CustomerClient.class);
        Mockito.when(client.getCustomer(Mockito.anyInt())).thenReturn(Customer.builder().firstName("FN").build());
        AspectJProxyFactory factory = new AspectJProxyFactory(client);
        MyAspect aspect = new MyAspect();
        factory.addAspect(aspect);
        CustomerClient proxy = factory.getProxy();
        proxy.getCustomer(2);
    }
}

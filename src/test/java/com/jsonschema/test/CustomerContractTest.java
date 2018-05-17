package com.jsonschema.test;

import com.jsonschema.aop.MyAspect;
import com.jsonschema.aop.MyTarget;
import com.jsonschema.web.client.CustomerClient;
import com.jsonschema.web.dto.Customer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

public class CustomerContractTest {



    @Test
    void testClient() {
        CustomerClient client = Mockito.mock(CustomerClient.class);
        Mockito.when(client.getCustomer(Mockito.anyInt())).thenReturn(Customer.builder().firstName("FN").lastName("last").build());
        AspectJProxyFactory factory = new AspectJProxyFactory(client);
        MyAspect aspect = new MyAspect();
        factory.addAspect(aspect);
        CustomerClient proxy = factory.getProxy();
        proxy.getCustomer(2);
    }
}

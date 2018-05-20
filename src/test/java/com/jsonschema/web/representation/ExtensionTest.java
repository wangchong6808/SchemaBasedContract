package com.jsonschema.web.representation;

import com.jsonschema.config.JsonSchemaExtension;
import com.jsonschema.web.client.CustomerClient;
import com.jsonschema.web.domain.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith({JsonSchemaExtension.class})
public class ExtensionTest {

    @MockBean
    OrderService orderService;

    @MockBean
    CustomerClient customerClient;

    @BeforeEach
    void setUp(){
        System.out.println("setup");
    }

    @Test
    void test1() {
        System.out.println("test1");
    }

    @Test
    void test2(){
        System.out.println("test2");
    }
}

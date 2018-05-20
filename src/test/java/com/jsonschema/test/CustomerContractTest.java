package com.jsonschema.test;

import com.jsonschema.aop.*;
import com.jsonschema.exception.SchemaViolatedException;
import com.jsonschema.web.client.CustomerClient;
import com.jsonschema.web.dto.Customer;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@ExtendWith(JsonSchemaExtension.class)
public class CustomerContractTest {


    @MockRemoteBean
    private CustomerClient customerClient;


    @Test
    void should_validate_successfully() {
        Mockito.when(customerClient.getCustomer(Mockito.anyInt())).thenReturn(Customer.builder().id(5).firstName("FN").lastName("last").build());
        SchemaDetector.detect(customerClient).getCustomer(2);
    }

    @Test
    void should_validate_failed() {
        Assertions.assertThrows(SchemaViolatedException.class, () -> {
            Mockito.when(customerClient.getCustomer(Mockito.anyInt())).thenReturn(Customer.builder().id(5).lastName("last").build());
            SchemaDetector.detect(customerClient).getCustomer(2);
            Assert.fail("schema is supposed to be violated because firstName is required.");
        });
    }

}

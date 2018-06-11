package com.jsonschema.web.representation;

import com.jsonschema.HelloWorldApplication;
import com.jsonschema.test.framework.aop.MockRemoteBean;
import com.jsonschema.web.client.CustomerClient;
import com.jsonschema.web.dto.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.jsonschema.test.framework.SchemaTestExecutionListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackageClasses = {HelloWorldApplication.class})
@TestExecutionListeners(listeners = {SchemaTestExecutionListener.class}, mergeMode = MERGE_WITH_DEFAULTS)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockRemoteBean
    CustomerClient customerClient;

    @BeforeEach
    void setUp() {

    }

    @Test
    void should_return_400_due_to_remote_schema_validation_failure() throws Exception {
        Mockito.when(customerClient.getCustomer(10)).thenReturn(Customer.builder().id(12).lastName("lastName").build());
        this.mockMvc.perform(post("/orders/10"))
                .andExpect(status().is(400));

    }

    @Test
    void should_return_400_due_to_controller_schema_validation_failure() throws Exception {
        Mockito.when(customerClient.getCustomer(8)).thenReturn(Customer.builder().id(8).mobile("123").firstName("first").lastName("lastName").build());
        this.mockMvc.perform(post("/orders/8"))
                .andExpect(status().is(400));

    }

    @Test
    void should_return_200_given_valid_customer_and_order_info() throws Exception {
        Mockito.when(customerClient.getCustomer(18)).thenReturn(Customer.builder().id(18).mobile("123").firstName("first").lastName("lastName").build());
        this.mockMvc.perform(post("/orders/18"))
                .andExpect(status().is(200));

    }
}
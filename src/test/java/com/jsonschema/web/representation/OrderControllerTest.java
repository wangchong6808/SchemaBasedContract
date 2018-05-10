package com.jsonschema.web.representation;

import com.jsonschema.HelloWorldApplication;
import com.jsonschema.web.domain.OrderService;
import com.jsonschema.web.dto.Customer;
import com.jsonschema.web.dto.Order;
import com.jsonschema.web.dto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackageClasses={HelloWorldApplication.class})
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @BeforeEach
    void setUp() {
        Mockito.when(orderService.getOrder(Mockito.anyInt())).thenAnswer(invocation -> {
            int orderId = Integer.valueOf(String.valueOf(invocation.getArguments()[0]));
            Product product1 = Product.builder().code("code1").name("手机").description("华为P20").build();
            Product product2 = Product.builder().code("code2").name("电池").description("华为P20电池").build();
            List<Product> products = new ArrayList<>();
            products.add(product1);
            products.add(product2);
            return Order.builder().id(orderId).customer(Customer.builder().lastName("张")
                    .mobile("123").build()).products(products).build();
        });
    }

    @Test
    void should_return_order() throws Exception {
        String s= this.mockMvc.perform(post("/order/10"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    void should_return_order_with_id_as_10() throws Exception {
        this.mockMvc.perform(post("/order/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.mobile").value("123"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void should_return_customer_of_order() throws Exception {
        this.mockMvc.perform(post("/order/10/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mobile").value("123"))
                .andReturn().getResponse().getContentAsString();
    }
}
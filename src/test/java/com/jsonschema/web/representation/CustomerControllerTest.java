package com.jsonschema.web.representation;

import com.jsonschema.HelloWorldApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackageClasses={HelloWorldApplication.class})
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_customer_by_client() throws Exception {
        String s= this.mockMvc.perform(get("/customer/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("10"))
                .andReturn().getResponse().getContentAsString();
    }

}

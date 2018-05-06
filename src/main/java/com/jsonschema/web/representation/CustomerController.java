package com.jsonschema.web.representation;

import com.jsonschema.annotation.JsonSchema;
import com.jsonschema.web.dto.Customer;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    @PostMapping("/customer")
    @JsonSchema(inputSchema = "customer_input", outputSchema = "customer_output")
    public Customer createCustomer(@PathVariable("id") String id, @RequestParam("firstName") String firstName, @RequestBody Customer customer) {
        customer.setMobile(id);
        customer.setFirstName(firstName);
        return customer;
    }

    @GetMapping("/customer/{id}")
    @JsonSchema(outputSchema = "customer_output")
    public Customer getCustomer(@PathVariable("id") int id) {
        return Customer.builder().id(id).mobile("123").lastName("Tom").build();
    }
}

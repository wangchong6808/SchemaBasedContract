package com.jsonschema.web.client;


import com.jsonschema.annotation.JsonSchema;
import com.jsonschema.web.dto.Customer;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "customer", url = "http://localhost:8080")
public interface CustomerClient extends SchemaAwareClient {


    /*@PostMapping(value = "/customer")
    Customer createCustomer(@PathVariable("id") String id, @RequestParam("firstName") String firstName, @RequestBody Customer customer);
*/
    @GetMapping("/customer/{id}")
    @JsonSchema(outputSchema = "customer_output")
    Customer getCustomer(@PathVariable("id") int id);
}

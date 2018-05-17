package com.jsonschema.web.client;


import com.jsonschema.annotation.JsonSchema;
import com.jsonschema.web.dto.Customer;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "customer", url = "http://localhost:8081")
public interface CustomerClient extends SchemaAwareClient {


    @PostMapping(value = "/customers")
    Customer createCustomer(@RequestParam("firstName") String firstName, @RequestBody Customer customer);

    @GetMapping("/customers/{id}")
    @JsonSchema(outputSchema = "1002", remoteSchema = "1002")
    Customer getCustomer(@PathVariable("id") Integer id);
}

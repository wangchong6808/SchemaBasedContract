/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jsonschema.web.representation;

import com.jsonschema.annotation.JsonSchema;
import com.jsonschema.web.client.CustomerClient;
import com.jsonschema.web.domain.OrderService;
import com.jsonschema.web.dto.Customer;
import com.jsonschema.web.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

	@Autowired
	CustomerClient customerClient;

    @Autowired
    OrderService orderService;


	@RequestMapping("/order/{id}")
	@ResponseBody
	@JsonSchema(outputSchema = "1003")
	public Order getOrder(@PathVariable("id") int id) {
	    Order order = orderService.getOrder(id);
		return order;
	}


	@RequestMapping("/order/{id}/customer")
	@ResponseBody
	@JsonSchema(outputSchema = "1003")
	public Customer getOrderCustomer(@PathVariable("id") int id) {
		return customerClient.getCustomer(id);
	}


}

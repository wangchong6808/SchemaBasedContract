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
package org.springframework.security.samples.web;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.samples.schema.annotation.JsonSchema;
import org.springframework.security.samples.web.dto.Customer;
import org.springframework.security.samples.web.dto.Order;
import org.springframework.security.samples.web.dto.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAspectJAutoProxy
public class MainController {

	@RequestMapping("/")
	public String root() {
		return "redirect:/index";
	}

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/user/index")
	public String userIndex() {
		return "user/index";
	}

	@RequestMapping("/user/role")
	public String userRole() {
		return "user/role";
	}

	@PostMapping("/customer/{id}")
	@JsonSchema(requestSchema = "input_customer")
	public Customer createCustomer(@PathVariable("id") String id, @RequestParam("firstName") String firstName, @RequestBody Customer customer) {
		customer.setMobile(id);
		customer.setFirstName(firstName);
		return customer;
	}


	@RequestMapping("/order/{id}")
	@ResponseBody
	@JsonSchema(responseSchema = "order_output")
	public void getOrder(@PathVariable("id") int id) {
		Product product1 = Product.builder().code("code1").name("手机").description("华为P20").build();
		Product product2 = Product.builder().name("电池").description("华为P20电池").build();
		List<Product> products = new ArrayList<>();
		products.add(product1);
		products.add(product2);
		//return Order.builder().id(id).customer(Customer.builder().lastName("张").build()).products(products).build();
	}

}

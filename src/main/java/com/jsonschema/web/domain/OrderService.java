package com.jsonschema.web.domain;

import com.jsonschema.web.client.CustomerClient;
import com.jsonschema.web.dto.Customer;
import com.jsonschema.web.dto.Order;
import com.jsonschema.web.dto.Product;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    CustomerClient customerClient;

    public Order getOrder(int orderId) {
        Product product1 = Product.builder().code("code1").name("手机").description("华为P20").build();
        Product product2 = Product.builder().name("电池").description("华为P20电池").build();
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        Order order = Order.builder().id(orderId).customer(Customer.builder().lastName("张").build()).products(products).build();
        order.setCustomer(customerClient.getCustomer(12));
        return order;
    }
}

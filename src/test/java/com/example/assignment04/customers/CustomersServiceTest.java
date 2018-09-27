package com.example.assignment04.customers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CustomersServiceTest {

    @Autowired
    private CustomersService customersService;

    @Autowired
    private CustomersRepository customersRepository;

    @Test
    public void createCustomer_expectCustomerCreatedWithDynamicID() {
        Customer createdCustomer = customersService.create(new Customer("customer 1", "customer 1 info"));
        List<Customer> customers = customersRepository.findAll();
        assertEquals(customers.get(0).getCustomerId(), createdCustomer.getCustomerId());
        assertEquals("customer 1", createdCustomer.getName());
    }

}

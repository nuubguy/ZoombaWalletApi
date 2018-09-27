package com.example.assignment04.customers;

import com.example.assignment04.accounts.AccountsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CustomersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private CustomersService customersService;

    @Autowired
    private AccountsRepository accountsRepository;


    @Test
    public void create_expectStatus201() throws Exception {
        Customer aCustomer = createNewCustomer1();
        this.mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aCustomer)))
                .andExpect(status().isCreated());
    }

    @Test
    public void create_expectNewCustomerCreated() throws Exception {
        Customer aCustomer = createNewCustomer1();
        MvcResult result = this.mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aCustomer)))
                .andExpect(status().isCreated()).andReturn();

        Customer createdCustomer = objectMapper.readValue(result.getResponse().getContentAsString(), Customer.class);
        assertEquals("customer 1", createdCustomer.getName());
    }

    @Test
    public void update_expect404_whenUpdateNonExistCustomer() throws Exception {
        Customer aCustomer = createNewCustomer2();
        this.mockMvc.perform(put("/customers/00000001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aCustomer)))
                .andExpect(status().isNotFound());

    }

    @Test
    public void update_expectUpdatedCustomer() throws Exception {
        Customer createdCustomer = createCustomer1InTheRepository();
        Customer aCustomer = createNewCustomer2();
        MvcResult result = this.mockMvc.perform(put("/customers/" + createdCustomer.getCustomerId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aCustomer)))
                .andExpect(status().isAccepted()).andReturn();

        Customer updatedCustomer = objectMapper.readValue(result.getResponse().getContentAsString(), Customer.class);
        assertEquals("customer 2", updatedCustomer.getName());

    }

    @Test
    public void delete_expect404_whenDeleteNonExistCustomer() throws Exception {
        this.mockMvc.perform(delete("/customers/00000001"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void delete_expectDeletedCustomer() throws Exception {
        Customer createdCustomer = createCustomer1InTheRepository();
        this.mockMvc.perform(delete("/customers/"+ createdCustomer.getCustomerId()))
                .andExpect(status().isAccepted());

        assertTrue(this.customersRepository.findAll().get(0).isDisabled());

    }

    @Test
    public void fetch_expect404_whenDeleteNonExistCustomer() throws Exception {
        this.mockMvc.perform(get("/customers/00000001"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void fetch_expectCustomer() throws Exception {
        Customer createdCustomer = createCustomer1InTheRepository();
        MvcResult result = this.mockMvc.perform(get("/customers/"+ createdCustomer.getCustomerId()))
                .andExpect(status().isOk()).andReturn();

        Customer targetCustomer = objectMapper.readValue(result.getResponse().getContentAsString(), Customer.class);
        assertEquals("customer 1", targetCustomer.getName());

    }

    @After
    public void cleanUp(){
        accountsRepository.deleteAll();
        customersRepository.deleteAll();
    }

    private Customer createNewCustomer1() {
        return new Customer("customer 1", "customer 1 info");
    }

    private Customer createCustomer1InTheRepository() {
        return customersService.create(createNewCustomer1());
    }

    private Customer createNewCustomer2() {
        return new Customer("customer 2", "customer 2 info");
    }
}

package com.zoombank.wallet_api.accounts;

import com.zoombank.wallet_api.Money;
import com.zoombank.wallet_api.customers.Customer;
import com.zoombank.wallet_api.customers.CustomersRepository;
import com.zoombank.wallet_api.customers.CustomersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private CustomersService customersService;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private AccountsRepository accountsRepository;

    @Test
    public void create_expectAccountWithBalanceCreatedForCustomer() throws Exception {
        Customer createdCustomer = createCustomer1InTheRepository();
        Account anAccount = createAccountWith5MilBalance(createdCustomer);
        MvcResult result = this.mockMvc.perform(post("/customers/" + createdCustomer.getCustomerId() + "/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anAccount)))
                .andExpect(status().isCreated()).andReturn();

        Account createdAccount = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);
        Assert.assertEquals(Double.valueOf("5000000"), createdAccount.getBalance().getAmount(),0);
    }

    @Test
    public void put_newListOfPayee() throws Exception {
        Customer createdCustomer = createCustomer1InTheRepository();
        Account anAccount = createAccountWith5MilBalanceInRepository(createdCustomer);
        Account account2 = createAccountWith5MilBalanceInRepository(createdCustomer);

        AccountPayeeRepresentation payLoad = new AccountPayeeRepresentation(anAccount);
        AccountSummaryRepresentation payee = new AccountSummaryRepresentation(account2);

        payLoad.getPayees().add(payee);

        String urlTemplate = "/customers/" + createdCustomer.getCustomerId() + "/accounts";
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payLoad)))
                .andExpect(status().isAccepted()).andDo(print()).andReturn();

        Account account = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);
        Assert.assertEquals(account.getPayees().size(), 1);
        assertEquals(account.getPayees().get(0).getAccountId(), account2.getAccountId());
    }

    @Test
    public void put_updateListOfPayee() throws Exception {
        Customer createdCustomer = createCustomer1InTheRepository();
        Account anAccount = createAccountWith5MilBalanceInRepository(createdCustomer);
        Account account2 = createAccountWith5MilBalanceInRepository(createdCustomer);
        Account account3 = createAccountWith5MilBalanceInRepository(createdCustomer);

        anAccount.getPayees().add(account2);
        accountsRepository.save(anAccount);

        AccountPayeeRepresentation payLoad = new AccountPayeeRepresentation(anAccount);
        payLoad.getPayees().clear();
        AccountSummaryRepresentation payee = new AccountSummaryRepresentation(account3);

        payLoad.getPayees().add(payee);

        String urlTemplate = "/customers/" + createdCustomer.getCustomerId() + "/accounts";
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payLoad)))
                .andExpect(status().isAccepted()).andDo(print()).andReturn();

        Account account = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);
        Assert.assertEquals(account.getPayees().size(), 1);
        assertEquals(account.getPayees().get(0).getAccountId(), account3.getAccountId());
    }

    @Test
    public void get_expectListOfPayee() throws Exception {
        Customer createdCustomer = createCustomer1InTheRepository();
        Account anAccount = createAccountWith5MilBalanceInRepository(createdCustomer);
        Account account2 = createAccountWith5MilBalanceInRepository(createdCustomer);

        anAccount.getPayees().add(account2);
        accountsRepository.save(anAccount);

        String urlTemplate = "/customers/" + createdCustomer.getCustomerId() + "/accounts/" + anAccount.getAccountId();
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate))
                .andExpect(status().isOk()).andDo(print()).andReturn();

        Account account = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);
        Assert.assertEquals(account.getPayees().size(), 1);
        assertEquals(account.getPayees().get(0).getAccountId(), account2.getAccountId());
    }


    @Test
    public void get_expectAccountIdAndCustomerName_WhenSearchByAccountId() throws Exception {
        Customer createdCustomer = createCustomer1InTheRepository();
        Account anAccount = createAccountWith5MilBalanceInRepository(createdCustomer);
        String urlTemplate = "/accounts?accountId=" + anAccount.getAccountId();
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate))
                .andExpect(status().isOk()).andDo(print()).andReturn();

        AccountSummaryRepresentation account = objectMapper.readValue(result.getResponse().getContentAsString(), AccountSummaryRepresentation.class);
        Assert.assertEquals(anAccount.getAccountId(), account.getAccountId());
        assertEquals(anAccount.getCustomer().getName(), account.getCustomerName());
    }


    @Test
    public void get_expectAccountWithBalance5Mil() throws Exception {
        Customer createdCustomer = createCustomer1InTheRepository();
        Account anAccount = createAccountWith5MilBalanceInRepository(createdCustomer);
        String urlTemplate = "/customers/" + createdCustomer.getCustomerId() + "/accounts/" + anAccount.getAccountId();
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate))
                .andExpect(status().isOk()).andDo(print()).andReturn();

        Account account = objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);
        Assert.assertEquals(Double.valueOf("5000000"), account.getBalance().getAmount(),0);
    }

    @Test
    public void get_expect404_whenGetBalanceForCustomerWhoDoesNotHaveAccount() throws Exception {
        Customer createdCustomer = createCustomer1InTheRepository();
        String urlTemplate = "/customers/" + createdCustomer.getCustomerId() + "/accounts/00000001";
        this.mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate))
                .andExpect(status().isNotFound());

    }

    @After
    public void cleanUp(){

        accountsRepository.deleteAllInBatch();
        customersRepository.deleteAll();
    }

    private Customer createNewCustomer1() {
        return new Customer("customer 1", "customer 1 info");
    }

    private Account createAccountWith5MilBalance(Customer customer) {
        return new Account(Money.indonesianRupiah(5000000), customer);
    }

    private Account createAccountWith5MilBalanceInRepository(Customer customer) {
        return accountsService.create(createAccountWith5MilBalance(customer));
    }

    private Customer createCustomer1InTheRepository() {
        return customersService.create(createNewCustomer1());
    }
}


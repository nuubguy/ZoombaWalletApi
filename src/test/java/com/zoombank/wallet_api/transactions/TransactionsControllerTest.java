package com.zoombank.wallet_api.transactions;

import com.zoombank.wallet_api.Money;
import com.zoombank.wallet_api.accounts.Account;
import com.zoombank.wallet_api.accounts.AccountsRepository;
import com.zoombank.wallet_api.accounts.AccountsService;
import com.zoombank.wallet_api.customers.Customer;
import com.zoombank.wallet_api.customers.CustomersRepository;
import com.zoombank.wallet_api.customers.CustomersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private CustomersService customersService;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private TransactionsService transactionsService;


    @Test
    public void create_expectStatus201() throws Exception {
        Account account1 = createAccount1With5MilBalanceInRepository();
        Account account2 = createAccount2With5MilBalanceInRepository();
        Transaction transferMoney = new Transaction(account1,account2, Money.indonesianRupiah(1000000));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferMoney)))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    public void create_expectStatus201_andHaveDescription() throws Exception {
        Account account1 = createAccount1With5MilBalanceInRepository();
        Account account2 = createAccount2With5MilBalanceInRepository();
        Transaction transferMoney = new Transaction(account1,account2, Money.indonesianRupiah(1000000));
        transferMoney.setDescription("Beli mobil");

        MvcResult result =  this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferMoney)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Beli mobil"));
    }

    @Test
    public void create_expectBalanceDebitedAndCredited_whenTransactionSuccess() throws Exception {
        Account account1 = createAccount1With5MilBalanceInRepository();
        Account account2 = createAccount2With5MilBalanceInRepository();
        Transaction transferMoney = new Transaction(account1, account2, Money.indonesianRupiah(1000000));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferMoney)))
                .andExpect(status().isCreated()).andDo(print());

        Money account1Balance = this.accountsService.getBalance(account1.getCustomer().getCustomerId(), account1.getAccountId());
        Money account2Balance = this.accountsService.getBalance(account2.getCustomer().getCustomerId(), account2.getAccountId());
        assertEquals(Double.valueOf("4000000"), account1Balance.getAmount(), 0);
        assertEquals(Double.valueOf("6000000"), account2Balance.getAmount(), 0);
    }

    @Test
    public void create_expect403_whenBalanceForTransactionIsNotSufficient() throws Exception {
        Account account1 = createAccount1With5MilBalanceInRepository();
        Account account2 = createAccount2With5MilBalanceInRepository();
        Transaction transferMoney = new Transaction(account1, account2, Money.indonesianRupiah(6000000));

        MvcResult result = this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferMoney)))
                .andExpect(status().isForbidden()).andDo(print()).andReturn();
        assertEquals("Insufficient balance", result.getResponse().getErrorMessage());

    }

    @Test
    public void create_expect404_whenAccountIsNotFound() throws Exception {
        createAccount1With5MilBalanceInRepository();
        createAccount2With5MilBalanceInRepository();

        MvcResult result = this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getInvalidAccountTransactionJSON()))
                .andExpect(status().isNotFound()).andDo(print()).andReturn();
        assertEquals("Account not found", result.getResponse().getErrorMessage());

    }

    @Test
    public void create_expect404_whenCustomerIsNotFound() throws Exception {
        Account account1 = createAccount1With5MilBalanceInRepository();
        Account account2 = createAccount2With5MilBalanceInRepository();

        MvcResult result = this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getInvalidCustomerTransactionJSON(account1, account2)))
                .andExpect(status().isNotFound()).andDo(print()).andReturn();
        assertEquals("Customer not found", result.getResponse().getErrorMessage());

    }

    private String getInvalidCustomerTransactionJSON(Account account1, Account account2) {
        return "{\"debit\":{\"balance\":{\"amount\":5000000.0,\"currency\":\"IDR\"},\"customer\":{\"name\":\"customer 1\",\"info\":\"customer 1 info\",\"customerId\":\"C00000001\",\"disabled\":false},\"accountId\":\"" + account1.getAccountId() + "\"},\"credit\":{\"balance\":{\"amount\":5000000.0,\"currency\":\"IDR\"},\"customer\":{\"name\":\"customer 2\",\"info\":\"customer 2 info\",\"customerId\":\"C00000003\",\"disabled\":false},\"accountId\":\"" + account2.getAccountId() +"\"},\"transactionAmount\":{\"amount\":6000000.0,\"currency\":\"IDR\"},\"transactionId\":null}";
    }

    @Test
    public void get_expectTransactionListForSpecifiedAccount() throws Exception {
        Account account1 = createAccount1With5MilBalanceInRepository();
        Account account2 = createAccount2With5MilBalanceInRepository();
        transfer(account1, account2, 1000000);

        MvcResult result = this.mockMvc.perform(get("/transactions?accountId=" + account1.getAccountId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).andReturn();

        assertEquals(objectMapper.writeValueAsString(transactionsRepository.findAll()), result.getResponse().getContentAsString());

    }


    @Test
    public void get_expect5LatestTransactionsForSpecifiedAccount() throws Exception {
        Account account1 = createAccount1With5MilBalanceInRepository();
        Account account2 = createAccount2With5MilBalanceInRepository();
        List<Transaction> transactions = new ArrayList<>();

        //create transactions
        Transaction trx1 = transfer(account1, account2, 500000);
        Transaction trx2 = transfer(account1, account2, 500000);
        Transaction trx3 = transfer(account1, account2, 500000);
        Transaction trx4 = transfer(account1, account2, 500000);
        Transaction trx5 = transfer(account1, account2, 500000);
        Transaction trx6 = transfer(account1, account2, 500000);
        Transaction trx7 = transfer(account1, account2, 500000);

        //add last five
        transactions.add(trx7);
        transactions.add(trx6);
        transactions.add(trx5);
        transactions.add(trx4);
        transactions.add(trx3);



        MvcResult result = this.mockMvc.perform(get("/transactions?accountId=" + account1.getAccountId() + "&limitResultFromLatest=5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).andReturn();

        assertEquals(objectMapper.writeValueAsString(transactions), result.getResponse().getContentAsString());

    }


    @Test
    public void create_expectBalanceCredited_whenTransactionWithoutDebitAccount() throws Exception {
        Account account1 = createAccount1With5MilBalanceInRepository();
        Transaction transferMoney = new Transaction(null, account1, Money.indonesianRupiah(1000000));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferMoney)))
                .andExpect(status().isCreated()).andDo(print());

        Money account1Balance = this.accountsService.getBalance(account1.getCustomer().getCustomerId(), account1.getAccountId());
        assertEquals(Double.valueOf("6000000"), account1Balance.getAmount(), 0);
    }

    @Test
    public void create_expectBalanceDebited_whenTransactionWithoutCreditAccount() throws Exception {
        Account account1 = createAccount1With5MilBalanceInRepository();
        Transaction transferMoney = new Transaction(account1, null, Money.indonesianRupiah(1000000));

        this.mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferMoney)))
                .andExpect(status().isCreated()).andDo(print());

        Money account1Balance = this.accountsService.getBalance(account1.getCustomer().getCustomerId(), account1.getAccountId());
        assertEquals(Double.valueOf("4000000"), account1Balance.getAmount(), 0);
    }

    private Transaction transfer(Account source, Account target, int amount) {
        Transaction transferMoney = new Transaction(source, target, Money.indonesianRupiah(amount));

        return transactionsService.create(transferMoney);
    }


    @After
    public void cleanUp(){
        transactionsRepository.deleteAll();
        accountsRepository.deleteAll();
        customersRepository.deleteAll();
    }

    private String getInvalidAccountTransactionJSON(){
        return "{\"debit\":{\"balance\":{\"amount\":5000000.0,\"currency\":\"IDR\"},\"customer\":{\"name\":\"customer 1\",\"info\":\"customer 1 info\",\"customerId\":\"C00000001\",\"disabled\":false},\"accountId\":\"A00000001\"},\"credit\":{\"balance\":{\"amount\":5000000.0,\"currency\":\"IDR\"},\"customer\":{\"name\":\"customer 2\",\"info\":\"customer 2 info\",\"customerId\":\"C00000002\",\"disabled\":false},\"accountId\":\"A00000003\"},\"transactionAmount\":{\"amount\":6000000.0,\"currency\":\"IDR\"},\"transactionId\":null}";

    }

    private Customer createNewCustomer1() {
        return customersService.create(new Customer("customer 1", "customer 1 info"));
    }

    private Account createAccount1With5MilBalanceInRepository() {
        return accountsService.create(createAccountWith5MilBalance(customersService.create(createNewCustomer1())));
    }

    private Account createAccount2With5MilBalanceInRepository() {
        return accountsService.create(createAccountWith5MilBalance(customersService.create(createNewCustomer2())));
    }

    private Account createAccountWith5MilBalance(Customer customer) {
        return new Account(Money.indonesianRupiah(5000000), customer);
    }

    private Customer createNewCustomer2() {
        return customersService.create(new Customer("customer 2", "customer 2 info"));
    }
}

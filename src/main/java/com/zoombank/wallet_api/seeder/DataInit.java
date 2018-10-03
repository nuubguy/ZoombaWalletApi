package com.zoombank.wallet_api.seeder;

import com.zoombank.wallet_api.Money;
import com.zoombank.wallet_api.accounts.Account;
import com.zoombank.wallet_api.accounts.AccountsService;
import com.zoombank.wallet_api.customers.Customer;
import com.zoombank.wallet_api.customers.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataInit {

    @Autowired
    CustomersService customersService;

    @Autowired
    AccountsService accountsService;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        initAccount();
    }

    private void initAccount() {
        String defaultPassword = "P@ssw0rd";

        Customer aCustomer = new Customer("Chip", "Someone at BTPN");
        customersService.create(aCustomer);

        Account newAccount = new Account(Money.indonesianRupiah(0),aCustomer);
        newAccount.setPassword(defaultPassword);
        accountsService.create(newAccount);

        Customer andries = new Customer("Andries", "CHIP member");
        customersService.create(andries);

        Account andriesAccount = new Account(Money.indonesianRupiah(0),andries);
        andriesAccount.setPassword(defaultPassword);
        accountsService.create(andriesAccount);

        Customer rifki = new Customer("Rifki", "CHIP member");
        customersService.create(rifki);

        Account rifkiAccount = new Account(Money.indonesianRupiah(0),rifki);
        rifkiAccount.setPassword(defaultPassword);
        accountsService.create(rifkiAccount);

    }
}

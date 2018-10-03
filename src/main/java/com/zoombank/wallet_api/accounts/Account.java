package com.zoombank.wallet_api.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zoombank.wallet_api.Money;
import com.zoombank.wallet_api.customers.Customer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static com.zoombank.wallet_api.customers.Customer.PASSWORD_ENCODER;

/**
 * Represent financial record of customer
 */
@Entity
public class Account {

    @Column
    @NotNull
    private double amount;

    @Column
    @NotNull
    private Currency currency;

    @ManyToOne
    private Customer customer;

    @Id
    private String accountId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Account> payees;

    @JsonIgnore
    @Column
    private String password;

    public Account(){

    }

    public List<Account> getPayees() {
        return payees;
    }

    public void setPayees(List<Account> payees) {
        this.payees = payees;
    }

    public Account(Money balance, Customer customer){

        setInternalBalance(balance);
        this.customer = customer;
        this.payees = new ArrayList<>();
        this.setPassword("P@ssw0rd");
    }

    private void setInternalBalance(Money balance) {
        this.amount = balance.getAmount();
        this.currency = balance.getCurrency();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public Money getBalance() {
        return new Money(this.amount,this.currency);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setBalance(Money balance) {
        setInternalBalance(balance);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public AccountSummaryRepresentation getRepresentation() {
        return new AccountSummaryRepresentation(this);
    }
}

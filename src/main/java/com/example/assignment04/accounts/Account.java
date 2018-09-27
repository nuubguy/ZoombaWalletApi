package com.example.assignment04.accounts;

import com.example.assignment04.Money;
import com.example.assignment04.customers.Customer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Currency;

/**
 * Represent financial record of customer
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "accountId")
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

    public Account(){

    }

    public Account(Money balance, Customer customer){

        setInternalBalance(balance);
        this.customer = customer;
    }

    private void setInternalBalance(Money balance) {
        this.amount = balance.getAmount();
        this.currency = balance.getCurrency();
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
}

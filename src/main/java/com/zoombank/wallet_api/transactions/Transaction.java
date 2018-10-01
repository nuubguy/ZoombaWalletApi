package com.zoombank.wallet_api.transactions;

import com.zoombank.wallet_api.Money;
import com.zoombank.wallet_api.accounts.Account;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Currency;

/**
 * Represent record of exchange money
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "transactionId")
public class Transaction {

    @ManyToOne(targetEntity = Account.class)
    @NotNull
    private Account debit;

    @ManyToOne(targetEntity = Account.class)
    @NotNull
    private Account credit;

    @Column
    @NotNull
    private double amount;

    @Column
    @NotNull
    private Currency currency;

    @Id
    private String transactionId;

    @Column
    @NotNull
    private LocalDateTime dateTime;

    @Column
    @NotNull
    @Size(max = 15,message = "Description cannot be more than 15 character")
    private String description = "";

    public Transaction(){

    }

    public Transaction(Account debit, Account credit, Money transactionAmount){

        this.debit = debit;
        this.credit = credit;
        setInternalAmount(transactionAmount);
    }

    private void setInternalAmount(Money transactionAmount) {
        this.amount = transactionAmount.getAmount();
        this.currency = transactionAmount.getCurrency();
    }

    public Account getDebit() {
        return debit;
    }

    public void setDebit(Account debit) {
        this.debit = debit;
    }

    public Account getCredit() {
        return credit;
    }

    public void setCredit(Account credit) {
        this.credit = credit;
    }

    public Money getTransactionAmount() {
        return new Money(this.amount, this.currency);
    }

    public void setTransactionAmount(Money transactionAmount) {
        setInternalAmount(transactionAmount);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

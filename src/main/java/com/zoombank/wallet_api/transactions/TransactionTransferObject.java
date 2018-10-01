package com.zoombank.wallet_api.transactions;

import com.zoombank.wallet_api.Money;
import com.zoombank.wallet_api.accounts.Account;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Currency;

/**
 * Represent Rest object for Transcation
 */
public class TransactionTransferObject {

    private String debitAccountId;

    private String creditAccountId;

    @NotNull
    private double amount;

    @NotNull
    private Currency currency;

    private String transactionId;

    private LocalDateTime dateTime;

    @Size(max = 15,message = "Description cannot be more than 15 character")
    private String description = "";

    public TransactionTransferObject(){

    }

    public TransactionTransferObject(Account debit, Account credit, Money transactionAmount){
        String debitAccountId = "";
        String creditAccountId = "";

        if(debit != null){
            debitAccountId = debit.getAccountId();
        }

        if(credit != null){
            creditAccountId = credit.getAccountId();
        }

        this.debitAccountId = debitAccountId;
        this.creditAccountId = creditAccountId;
        setInternalAmount(transactionAmount);
    }

    private void setInternalAmount(Money transactionAmount) {
        this.amount = transactionAmount.getAmount();
        this.currency = transactionAmount.getCurrency();
    }

    public String getDebitAccountId() {
        return debitAccountId;
    }

    public void setDebitAccountId(String debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public String getCreditAccountId() {
        return creditAccountId;
    }

    public void setCreditAccountId(String creditAccountId) {
        this.creditAccountId = creditAccountId;
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

    @AssertTrue(message = "Must specified Debit or Credit Account")
    private boolean isDebitCreditValid(){
        return ((this.creditAccountId != null && !this.creditAccountId.isEmpty()) || (this.debitAccountId != null && !this.debitAccountId.isEmpty()));

    }
}

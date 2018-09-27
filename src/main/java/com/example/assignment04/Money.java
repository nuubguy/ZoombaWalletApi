package com.example.assignment04;

import java.util.Currency;
import java.util.Locale;

/**
 * Represent resource that can be used as medium of exchange
 */
public class Money {

    private double amount;
    private Currency currency;

    public Money(double amount, Currency currency){
        checkAmount(amount);
        this.amount = amount;
        this.currency = currency;
    }

    private void checkAmount(double amount) throws InvalidAmountOfMoneyException {
        if(amount < 0){
            throw new InvalidAmountOfMoneyException();
        }
    }

    public Money(){

    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public static Money indonesianRupiah(double amount){
        Locale locale = new Locale("in", "ID");
        Currency currency = Currency.getInstance(locale);
        return new Money(amount, currency);
    }

    public void setAmount(double amount) {
        checkAmount(amount);
        this.amount = amount;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Money subtract(Money transactionAmount) {
        checkCurrency(transactionAmount.getCurrency());
        return new Money(this.getAmount() - transactionAmount.getAmount(), this.currency);

    }

    private void checkCurrency(Currency other) {
        if(this.currency != other){
            throw new DifferentCurrencyException();
        }
    }

    public Money add(Money transactionAmount) {
        checkCurrency(transactionAmount.getCurrency());
        return new Money(this.getAmount() + transactionAmount.getAmount(), this.currency);
    }
}

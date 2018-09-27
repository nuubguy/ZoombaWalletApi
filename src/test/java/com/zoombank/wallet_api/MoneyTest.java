package com.zoombank.wallet_api;

import org.junit.Test;

import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.*;

public class MoneyTest {

    @Test(expected = InvalidAmountOfMoneyException.class)
    public void indonesianRupiah_expectException_WhenAmountLessThanZero() {
        Money.indonesianRupiah(-1);
    }

    @Test
    public void indonesianRupiah_expectMoneyWithBalance1000() {
        Money money = Money.indonesianRupiah(1000);

        assertEquals(Double.valueOf("1000"), money.getAmount(),0);
    }

    @Test
    public void add_expectMoneyWithBalance1000_whenBalanceOf500Add500() {
        Money money = Money.indonesianRupiah(1000);
        Money newBalance = Money.indonesianRupiah(500).add(Money.indonesianRupiah(500));

        assertEquals(newBalance.getAmount(), money.getAmount(),0);
    }

    @Test
    public void add_expectMoneyWithBalance1000_whenBalanceOf1500Subtract500() {
        Money money = Money.indonesianRupiah(1000);
        Money newBalance = Money.indonesianRupiah(1500).subtract(Money.indonesianRupiah(500));

        assertEquals(newBalance.getAmount(), money.getAmount(),0);
    }

    @Test(expected = DifferentCurrencyException.class)
    public void add_expectException_whenAddDifferentCurrency() {
        Locale locale = new Locale("en", "US");
        Money.indonesianRupiah(500).add(new Money(500, Currency.getInstance(locale)));
    }
}

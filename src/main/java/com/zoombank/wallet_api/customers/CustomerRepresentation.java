package com.zoombank.wallet_api.customers;

import com.zoombank.wallet_api.accounts.Account;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepresentation {

    private String customerId;
    private String customerName;
    private List<Account> accountList;

    public CustomerRepresentation (String customerId, String customerName){
        this.customerId = customerId;
        this.customerName = customerName;
        this.accountList = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }
}

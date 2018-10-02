package com.zoombank.wallet_api.accounts;

/**
 * Represent account for REST summary view
 */
public class AccountSummaryRepresentation {

    private String accountId;
    private String customerName;

    public AccountSummaryRepresentation(){

    }

    public AccountSummaryRepresentation(Account account){
        this.accountId = account.getAccountId();
        this.customerName = account.getCustomer().getName();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}

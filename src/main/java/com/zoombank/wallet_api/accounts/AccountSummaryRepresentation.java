package com.zoombank.wallet_api.accounts;

/**
 * Represent account for REST summary view
 */
public class AccountSummaryRepresentation {

    private String accountId;
    private String customerName;
    private String customerId;

    public AccountSummaryRepresentation(){

    }

    public AccountSummaryRepresentation(Account account){
        this.accountId = account.getAccountId();
        this.customerName = account.getCustomer().getName();
        this.customerId = account.getCustomer().getCustomerId();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

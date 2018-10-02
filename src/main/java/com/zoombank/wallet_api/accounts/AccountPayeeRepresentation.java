package com.zoombank.wallet_api.accounts;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent REST for updating payees in account
 */
public class AccountPayeeRepresentation {

    private String accountId;
    private List<AccountSummaryRepresentation> payees;

    public AccountPayeeRepresentation(){

    }
    public AccountPayeeRepresentation(Account account){
        this.accountId = account.getAccountId();
        this.payees = new ArrayList<>();
        for (int i = 0; i < account.getPayees().size(); i++) {
            payees.add(account.getPayees().get(i).getRepresentation());
        }
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<AccountSummaryRepresentation> getPayees() {
        return payees;
    }

    public void setPayees(List<AccountSummaryRepresentation> payees) {
        this.payees = payees;
    }
}

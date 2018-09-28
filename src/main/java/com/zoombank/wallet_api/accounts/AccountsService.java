package com.zoombank.wallet_api.accounts;

import com.zoombank.wallet_api.BaseService;
import com.zoombank.wallet_api.Money;
import com.zoombank.wallet_api.customers.Customer;
import com.zoombank.wallet_api.customers.CustomerNotFoundException;
import com.zoombank.wallet_api.customers.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service stereotype for Accounts
 */
@Service
public class AccountsService extends BaseService<Account> {

    private static final String idPrefix = "A";
    private final String cashAccountId = "CASH ACCOUNT";

    @Autowired
    CustomersService customersService;

    @Autowired
    AccountsRepository accountsRepository;

    public Account getBankCashAccount(){
        Account result;
        try {
            result = this.getById(this.cashAccountId);
        }
        catch (InvalidAccountException ex){
            result = new Account(Money.indonesianRupiah(Double.valueOf("10000000000000")), this.customersService.getBankCashAccountCustomer());
            result.setAccountId(this.cashAccountId);
            accountsRepository.save(result);
        }
        return result;
    }

    @Override
    public Account create(Account target) {
        target.setAccountId(this.createID());
        this.accountsRepository.save(target);
        return target;
    }

    @Override
    protected String preFix() {
        return idPrefix;
    }

    public Account getById(String id) {
        Optional<Account> result = this.accountsRepository.findById(id);
        if(!result.isPresent()) throw new InvalidAccountException();
        return result.get();
    }

    public Account create(String id, Account account) {
        Customer customer = this.customersService.getById(id);
        account.setCustomer(customer);
        return this.create(account);

    }

    public Money getBalance(String customerId, String accountId) {
        Account account = this.getById(accountId);
        if(!account.getCustomer().getCustomerId().equals(customerId)){
            throw new CustomerNotFoundException();
        }

        return account.getBalance();
    }


    public void updateBalance(Account accountWithNewBalance) {
        this.accountsRepository.save(accountWithNewBalance);
    }
}

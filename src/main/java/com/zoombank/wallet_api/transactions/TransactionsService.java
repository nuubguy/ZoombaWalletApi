package com.zoombank.wallet_api.transactions;

import com.zoombank.wallet_api.BaseService;
import com.zoombank.wallet_api.InvalidAmountOfMoneyException;
import com.zoombank.wallet_api.Money;
import com.zoombank.wallet_api.accounts.Account;
import com.zoombank.wallet_api.accounts.AccountsService;
import com.zoombank.wallet_api.accounts.InvalidAccountException;
import com.zoombank.wallet_api.customers.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service stereotype for transactions
 */
@Service
public class TransactionsService extends BaseService<Transaction> {

    private static final String idPrefix = "T";

    @Autowired
    AccountsService accountsService;

    @Autowired
    TransactionsRepository transactionsRepository;

    private Transaction fillBankCashAccount(Transaction transaction){
        if(transaction.getCredit() == null && transaction.getDebit() ==null) {
            throw new InvalidTransactionAccountNotFoundException();
        }
        if(transaction.getDebit() == null){
            transaction.setDebit(this.accountsService.getBankCashAccount());
        }
        if(transaction.getCredit() == null){
            transaction.setCredit(this.accountsService.getBankCashAccount());
        }

        return transaction;
    }

    private Transaction removeBankCashAccount(Transaction transaction){
        if(transaction.getDebit().getAccountId().equals(accountsService.getBankCashAccount().getAccountId())){
            transaction.setDebit(null);
        }
        if(transaction.getCredit().getAccountId().equals(accountsService.getBankCashAccount().getAccountId())){
            transaction.setCredit(null);
        }

        return transaction;
    }

    @Override
    public Transaction create(Transaction target) {
        fillBankCashAccount(target);

        Money debitBalance = getTargetTransactionBalance(target.getDebit());
        Money creditBalance = getTargetTransactionBalance(target.getCredit());

        try {
            target.getDebit().setBalance(debitBalance.subtract(target.getTransactionAmount()));
            target.getCredit().setBalance(creditBalance.add(target.getTransactionAmount()));

            accountsService.updateBalance(target.getDebit());
            accountsService.updateBalance(target.getCredit());

            target.setDateTime(LocalDateTime.now());
            target.setTransactionId(this.createID());
            transactionsRepository.save(target);

            removeBankCashAccount(target);
        }
        catch (InvalidAmountOfMoneyException ex) {
            throw new InsufficientBalanceTransactionException(ex);
        }

        return target;
    }

    private Money getTargetTransactionBalance(Account account) {
        try {
            return getBalance(account.getCustomer().getCustomerId(), account.getAccountId());
        }
        catch (CustomerNotFoundException ce){
            throw new InvalidTransactionCustomerNotFoundException();
        }

        catch (InvalidAccountException ae){
            throw new InvalidTransactionAccountNotFoundException();
        }
    }

    private Money getBalance(String customerId, String accountId) {
        return this.accountsService.getBalance(customerId, accountId);
    }

    @Override
    protected String preFix() {
        return idPrefix;
    }

    public List<Transaction> fetchAllByAccount(String accountId) {
        List<Transaction> collect = new ArrayList<>();
        for (Transaction transaction : this.transactionsRepository.findAll()) {
            if (transaction.getDebit().getAccountId().equals(accountId) || transaction.getCredit().getAccountId().equals(accountId)) {
                collect.add(transaction);
            }
        }
        return collect;

    }

    public List<Transaction> fetchLatestByAccount(String accountId, Integer limitResultFromLatest){
        return this.transactionsRepository.fecthLatestByAccountId(accountId, PageRequest.of(0,limitResultFromLatest)).getContent();

    }
}
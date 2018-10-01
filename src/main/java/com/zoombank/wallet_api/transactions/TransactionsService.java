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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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

    public Transaction create(TransactionRepresentation target){
        Account debit = null;
        Account credit = null;

        if(!target.getCreditAccountId().isEmpty()){
            credit = accountsService.getById(target.getCreditAccountId());
        }

        if(!target.getDebitAccountId().isEmpty()){
            debit = accountsService.getById(target.getDebitAccountId());
        }

        Money transactionAmount = target.getTransactionAmount();

        Transaction aTransaction = new Transaction(debit, credit, transactionAmount);
        aTransaction.setDescription(target.getDescription());

        return create(aTransaction);
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
            if (target.getCredit().getAccountId().equals(this.accountsService.cashAccountId)){
                target.setWithdrawalCode(getWithdrawalCode(target.getTransactionId()).toString());
            }
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
        return this.transactionsRepository.getAllByCredit_AccountIdOrDebit_AccountId(accountId, accountId);

    }
    public List<Transaction> fetchByDecsription(String accountId,String description) {
        return this.transactionsRepository.getAllByDescriptionContainingIgnoreCase(description);
    }

    public List<Transaction> fetchByAmount(String accountId, double amount) {
        return this.transactionsRepository.getAllByAmountEquals(amount);

    }

    public List<Transaction> fetchByAmountAndDescription(String accountId,String decription,double amount) {
        return this.transactionsRepository.getAllByDescriptionContainingIgnoreCaseAndAmountEquals
                (decription,amount);
    }

    public List<Transaction> fetchLatestByAccount(String accountId, Integer limitResultFromLatest){
        return this.transactionsRepository.getAllByCredit_AccountIdOrDebit_AccountIdOrderByDateTimeDesc(accountId, accountId, PageRequest.of(0,limitResultFromLatest)).getContent();
    }

    public Integer getWithdrawalCode(String transactionId) {
        String plaintext = transactionId;
        int hash = transactionId.hashCode();
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = bigInt.toString(10);
            while(hashtext.length() < 32 ){
                hashtext = "0"+hashtext;
            }
            int temp = 0;
            for(int i =0; i<hashtext.length();i++){
                char c = hashtext.charAt(i);
                temp+=(int)c;
            }
            return hash+temp;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hash;
    }
}

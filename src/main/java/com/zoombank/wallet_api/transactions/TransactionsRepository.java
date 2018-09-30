package com.zoombank.wallet_api.transactions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository stereotype for transactions
 */
@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, String> {

    List<Transaction> getAllByCredit_AccountIdOrDebit_AccountId(String creditAccountId, String debitAccountId);

    Page<Transaction> getAllByCredit_AccountIdOrDebit_AccountIdOrderByDateTimeDesc(String creditAccountId, String debitAccountId, Pageable pageable);

    List<Transaction>getAllByDescriptionContaining(String description);

    List<Transaction>getAllByAmountEquals(double amount);

    List<Transaction>getAllByDescriptionContainingAndAmountEquals(String description,double amount);

    List<Transaction>findAllByOrderByAmountDesc();


}

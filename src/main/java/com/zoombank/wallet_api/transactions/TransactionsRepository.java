package com.zoombank.wallet_api.transactions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository stereotype for transactions
 */
@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor {

    List<Transaction> getAllByCredit_AccountIdOrDebit_AccountId(String creditAccountId, String debitAccountId);

    Page<Transaction> getAllByCredit_AccountIdOrDebit_AccountIdOrderByDateTimeDesc(String creditAccountId, String debitAccountId, Pageable pageable);


    List<Transaction>getAllByDescriptionContainingIgnoreCase(String description);

    List<Transaction>getAllByAmountEquals(double amount);

    List<Transaction>getAllByDescriptionContainingIgnoreCaseAndAmountEquals(String description,double amount);

}

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

    List<Transaction>getAllByCredit_AccountIdOrDebit_AccountIdAndDescriptionContaining(String creditAccountId,
                                                                                       String debitAccountId,
                                                                                       String description);

    List<Transaction>getAllByCredit_AccountIdOrDebit_AccountIdAndAmountEquals(String creditAccountId,
                                                                              String debitAccountId,
                                                                              double amount);

    List<Transaction>getAllByCredit_AccountIdOrDebit_AccountIdAndDescriptionContainingAndAmountEquals
            (String creditAccountId,
             String debitAccountId,
             String description,
             double amount);

    List<Transaction>getAllByCredit_AccountIdOrDebit_AccountIdOrderByAmountDesc(String creditAccountId,
                                                                                String debitAccountId);


}

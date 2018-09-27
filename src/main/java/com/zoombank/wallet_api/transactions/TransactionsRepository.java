package com.zoombank.wallet_api.transactions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Repository stereotype for transactions
 */
@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, String> {

    @Query("SELECT t FROM Transaction t WHERE t.credit like concat('%', upper(?1), '%') or t.debit like concat('%', upper(?1), '%') order by t.dateTime desc ")
    Page<Transaction> fecthLatestByAccountId(String accountId, Pageable pageable);
}

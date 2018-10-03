package com.zoombank.wallet_api.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository stereotype for Accounts
 */
@Repository
public interface AccountsRepository extends JpaRepository<Account, String> {
    List<Account> getAllByCustomer_CustomerId(String customerId);
}

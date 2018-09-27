package com.example.assignment04.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository stereotype for Accounts
 */
@Repository
public interface AccountsRepository extends JpaRepository<Account, String> {

}

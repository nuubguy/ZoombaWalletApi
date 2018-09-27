package com.zoombank.wallet_api.customers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository stereotype for Customers
 */
@Repository
public interface CustomersRepository extends JpaRepository<Customer, String> {

}

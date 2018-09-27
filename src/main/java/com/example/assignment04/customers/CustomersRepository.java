package com.example.assignment04.customers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository stereotype for Customers
 */
@Repository
public interface CustomersRepository extends JpaRepository<Customer, String> {

}

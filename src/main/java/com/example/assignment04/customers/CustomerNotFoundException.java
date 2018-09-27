package com.example.assignment04.customers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represent condition when customer is not found in the repository
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Customer not found")
public
class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {
    }
}

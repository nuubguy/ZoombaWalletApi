package com.zoombank.wallet_api.transactions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the specified customer not found in the system
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Customer not found")
class InvalidTransactionCustomerNotFoundException extends RuntimeException {
    public InvalidTransactionCustomerNotFoundException() {
    }
}

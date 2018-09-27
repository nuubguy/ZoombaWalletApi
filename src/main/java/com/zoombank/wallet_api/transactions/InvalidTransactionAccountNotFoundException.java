package com.zoombank.wallet_api.transactions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the specified account not found in the system
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Account not found")
class InvalidTransactionAccountNotFoundException extends RuntimeException {
    public InvalidTransactionAccountNotFoundException() {
    }
}

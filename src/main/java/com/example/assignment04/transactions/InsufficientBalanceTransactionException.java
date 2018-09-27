package com.example.assignment04.transactions;

import com.example.assignment04.InvalidAmountOfMoneyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that thrown when transaction failed
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Insufficient balance")
class InsufficientBalanceTransactionException extends RuntimeException {
    public InsufficientBalanceTransactionException(InvalidAmountOfMoneyException ex) {
        super(ex);
    }
}

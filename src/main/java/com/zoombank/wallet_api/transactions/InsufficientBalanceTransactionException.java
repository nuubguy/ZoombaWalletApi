package com.zoombank.wallet_api.transactions;

import com.zoombank.wallet_api.InvalidAmountOfMoneyException;
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

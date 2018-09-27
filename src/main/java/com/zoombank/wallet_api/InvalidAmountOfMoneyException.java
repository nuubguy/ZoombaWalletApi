package com.zoombank.wallet_api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that thrown when amount of money have negative
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Invalid amount")
public class InvalidAmountOfMoneyException extends RuntimeException {
}

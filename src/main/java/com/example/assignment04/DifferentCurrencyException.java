package com.example.assignment04;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that thrown add or subtract different currency
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Different currency")
class DifferentCurrencyException extends RuntimeException {
}

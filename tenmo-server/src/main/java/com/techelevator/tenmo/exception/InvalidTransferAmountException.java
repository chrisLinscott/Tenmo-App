package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST, reason = "Transfer amount must be greater than 0")
public class InvalidTransferAmountException extends RuntimeException {

}

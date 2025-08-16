package org.example.wallet.exception;

import org.example.wallet.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ApiError> handleWalletNotFoundException(WalletNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiError> handleInsufficientFundsException(InsufficientFundsException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiError> buildResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ApiError(message, status, ZonedDateTime.now()));
    }

}

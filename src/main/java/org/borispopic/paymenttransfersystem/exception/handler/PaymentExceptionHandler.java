package org.borispopic.paymenttransfersystem.exception.handler;

import org.borispopic.paymenttransfersystem.exception.DestinationAccountNotFoundException;
import org.borispopic.paymenttransfersystem.exception.InsufficientFundsException;
import org.borispopic.paymenttransfersystem.exception.SourceAccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class PaymentExceptionHandler {
    @ExceptionHandler(SourceAccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSourceAccountNotFound(SourceAccountNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DestinationAccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDestinationAccountNotFound(DestinationAccountNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientFunds(InsufficientFundsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return new ResponseEntity<>(error, status);
    }
}

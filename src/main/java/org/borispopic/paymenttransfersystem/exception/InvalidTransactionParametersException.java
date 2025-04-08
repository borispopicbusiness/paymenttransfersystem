package org.borispopic.paymenttransfersystem.exception;

public class InvalidTransactionParametersException extends RuntimeException {
    public InvalidTransactionParametersException(String message) {
        super(message);
    }
}


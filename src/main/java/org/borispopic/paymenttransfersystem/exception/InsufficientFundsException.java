package org.borispopic.paymenttransfersystem.exception;

public class InsufficientFundsException extends InvalidTransactionParametersException {
    public InsufficientFundsException() {
        super("Source account balance out of range");
    }
}

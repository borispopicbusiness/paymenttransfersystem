package org.borispopic.paymenttransfersystem.exception;

public class DestinationAccountNotFoundException extends InvalidTransactionParametersException {
    public DestinationAccountNotFoundException() {
        super("Destination account not found");
    }
}

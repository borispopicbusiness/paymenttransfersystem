package org.borispopic.paymenttransfersystem.exception;

public class SourceAccountNotFoundException extends InvalidTransactionParametersException {
    public SourceAccountNotFoundException() {
        super("Source account not found");
    }
}

package org.borispopic.paymenttransfersystem.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionRequestDTO {
    private BigDecimal amount;
    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private String comment;
}

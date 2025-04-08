package org.borispopic.paymenttransfersystem.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionRequestDTO {
    private BigDecimal amount;
    private String sourceAccountId;
    private String destinationAccountId;
    private String comment;
}

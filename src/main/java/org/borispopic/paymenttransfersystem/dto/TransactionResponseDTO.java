package org.borispopic.paymenttransfersystem.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class TransactionResponseDTO {
    private UUID id;
    private BigDecimal amount;
    private String sourceAccountId;
    private String destinationAccountId;
    private LocalDate entryDate;
    private String comment;
}

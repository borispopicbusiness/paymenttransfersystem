package org.borispopic.paymenttransfersystem.domain;

import lombok.Builder;
import lombok.Data;
import org.borispopic.paymenttransfersystem.enums.EntryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class Transaction {
    private UUID id;
    private BigDecimal amount;
    private String sourceAccountId;
    private String destinationAccountId;
    private LocalDate entryDate;
    private String comment;
}

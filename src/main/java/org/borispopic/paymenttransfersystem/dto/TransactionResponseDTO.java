package org.borispopic.paymenttransfersystem.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import org.borispopic.paymenttransfersystem.enums.EntryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class TransactionResponseDTO {
    private UUID id;
    private BigDecimal amount;
    private UUID sourceAccountId;
    private UUID destinationAccountId;
    private LocalDate entryDate;
    private String comment;
}

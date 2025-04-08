package org.borispopic.paymenttransfersystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.borispopic.paymenttransfersystem.enums.EntryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LedgerEntity {
    @Id
    private UUID id;
    private UUID transactionId;
    private BigDecimal amount;
    private EntryType type;
    private LocalDate entryDate;
}

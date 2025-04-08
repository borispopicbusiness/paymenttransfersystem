package org.borispopic.paymenttransfersystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.borispopic.paymenttransfersystem.enums.EntryType;
import org.borispopic.paymenttransfersystem.enums.converter.EntryTypeConverter;

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
    @GeneratedValue
    private UUID id;
    private UUID transactionId;
    private BigDecimal amount;
    @Convert(converter = EntryTypeConverter.class)
    private EntryType type;
    private LocalDate entryDate;
}

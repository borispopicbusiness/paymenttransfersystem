package org.borispopic.paymenttransfersystem.domain;

import lombok.Builder;
import lombok.Data;
import org.borispopic.paymenttransfersystem.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Account {
    private String accountNumber;
    private BigDecimal balance;
    private String currency;
    private LocalDate registered;
    private String owner;
    private AccountType accountType;
    private String comment;
}

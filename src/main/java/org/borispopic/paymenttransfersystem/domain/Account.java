package org.borispopic.paymenttransfersystem.domain;

import lombok.Builder;
import lombok.Data;
import org.borispopic.paymenttransfersystem.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

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

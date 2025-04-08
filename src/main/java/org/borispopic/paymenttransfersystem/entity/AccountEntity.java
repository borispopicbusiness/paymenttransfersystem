package org.borispopic.paymenttransfersystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.borispopic.paymenttransfersystem.enums.AccountType;
import org.borispopic.paymenttransfersystem.enums.converter.AccountTypeConverter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "accounts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {
    @Id
    private String accountNumber;
    private BigDecimal balance;
    private String currency;
    private LocalDate registered;
    private String owner;
    @Convert(converter = AccountTypeConverter.class)
    private AccountType accountType;
    private String comment;
}

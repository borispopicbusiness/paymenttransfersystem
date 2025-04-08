package org.borispopic.paymenttransfersystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.borispopic.paymenttransfersystem.enums.AccountType;

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
    private AccountType accountType;
    private String comment;
}

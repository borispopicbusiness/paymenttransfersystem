package org.borispopic.paymenttransfersystem.service.impl;

import org.borispopic.paymenttransfersystem.domain.Account;
import org.borispopic.paymenttransfersystem.entity.AccountEntity;
import org.borispopic.paymenttransfersystem.enums.AccountType;
import org.borispopic.paymenttransfersystem.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceIntegrationTest {

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testCreateAccount() {
        Account newAccount = Account.builder()
                .accountNumber("RS22NR85VPNEFEA02X")
                .balance(BigDecimal.valueOf(500.00))
                .currency("RSD")
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .accountType(AccountType.PERSONAL)
                .comment("Another test account")
                .build();

        Account savedAccount = accountService.createAccount(newAccount).get();
        assertNotNull(savedAccount);
        assertEquals(newAccount.getAccountNumber(), savedAccount.getAccountNumber());
    }

    @Test
    public void testDeleteAccount() {

        Account newAccount = Account.builder()
                .accountNumber("RS22NR85VPNEFEA02X")
                .balance(BigDecimal.valueOf(500.00))
                .currency("RSD")
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .accountType(AccountType.PERSONAL)
                .comment("Another test account")
                .build();

        accountService.deleteAccount(newAccount.getAccountNumber());
        Optional<AccountEntity> deletedAccount = accountRepository.findById(newAccount.getAccountNumber());
        assertFalse(deletedAccount.isPresent());
    }
}

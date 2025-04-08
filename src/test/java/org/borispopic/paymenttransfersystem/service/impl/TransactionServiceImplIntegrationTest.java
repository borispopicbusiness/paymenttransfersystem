package org.borispopic.paymenttransfersystem.service.impl;

import org.borispopic.paymenttransfersystem.domain.Transaction;
import org.borispopic.paymenttransfersystem.entity.AccountEntity;
import org.borispopic.paymenttransfersystem.entity.LedgerEntity;
import org.borispopic.paymenttransfersystem.enums.AccountType;
import org.borispopic.paymenttransfersystem.exception.InsufficientFundsException;
import org.borispopic.paymenttransfersystem.exception.SourceAccountNotFoundException;
import org.borispopic.paymenttransfersystem.repository.AccountRepository;
import org.borispopic.paymenttransfersystem.repository.LedgerRepository;
import org.borispopic.paymenttransfersystem.repository.TransactionRepository;
import org.borispopic.paymenttransfersystem.mapper.TransactionsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TransactionServiceImplIntegrationTest {

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private TransactionsMapper transactionsMapper;

    private AccountEntity sourceAccount;
    private AccountEntity destinationAccount;

    @BeforeEach
    void setUp() {
        // Setup initial test data for accounts
        sourceAccount = accountRepository.save(AccountEntity.builder()
                .accountNumber("RS22NR85VPNEFEA02X")
                .currency("EUR")
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .accountType(AccountType.PERSONAL)
                .balance(BigDecimal.valueOf(1000.50))
                .build()
        );
        destinationAccount = accountRepository.save(AccountEntity.builder()
                .accountNumber("RS11NR85VPNEFEA02X")
                .currency("EUR")
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .accountType(AccountType.PERSONAL)
                .balance(BigDecimal.valueOf(1000.50))
                .build()
        );
    }

    @Test
    void shouldSuccessfullyPerformTransaction() {
        // Create a transaction request
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(100.00))
                .sourceAccountId(sourceAccount.getAccountNumber())
                .destinationAccountId(destinationAccount.getAccountNumber())
                .comment("Test transaction")
                .build();

        // Perform the transaction through the service
        var result = transactionService.performTransaction(transaction);

        // Assert the transaction was successfully completed
        assertTrue(result.isPresent());
        Transaction transactionEntity = result.get();

        // Verify the transaction entity was saved
        assertNotNull(transactionEntity.getId());
        assertEquals(BigDecimal.valueOf(100.00), transactionEntity.getAmount());

        // Verify the account balances were updated
        sourceAccount = accountRepository.findById(sourceAccount.getAccountNumber()).orElseThrow();
        destinationAccount = accountRepository.findById(destinationAccount.getAccountNumber()).orElseThrow();
        assertEquals(BigDecimal.valueOf(900.50).setScale(2, RoundingMode.HALF_UP), sourceAccount.getBalance());
        assertEquals(BigDecimal.valueOf(1100.50).setScale(2, RoundingMode.HALF_UP), destinationAccount.getBalance());

        // Verify ledger entries
        List<LedgerEntity> ledgerList = ledgerRepository.findAll();
        assertEquals(2, ledgerList.size());
    }

    @Test
    void shouldExperienceFailurePerformTransaction() {
        // Create a transaction with insufficient funds in the source account
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(2000))  // More than the balance
                .sourceAccountId(sourceAccount.getAccountNumber())
                .destinationAccountId(destinationAccount.getAccountNumber())
                .comment("Test insufficient funds transaction")
                .build();

        assertThrows(InsufficientFundsException.class, () -> {
            transactionService.performTransaction(transaction);
        });
    }

    @Test
    void shouldFailPerformTransaction_InvalidSourceAccount() {
        // Create a transaction with an invalid source account
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(100))
                .sourceAccountId("invalidSourceAccount")
                .destinationAccountId(destinationAccount.getAccountNumber())
                .comment("Test invalid source account")
                .build();

        assertThrows(SourceAccountNotFoundException.class, () -> {
            transactionService.performTransaction(transaction);
        });
    }
}


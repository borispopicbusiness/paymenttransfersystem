package org.borispopic.paymenttransfersystem.service.impl;

import org.borispopic.paymenttransfersystem.domain.Transaction;
import org.borispopic.paymenttransfersystem.entity.AccountEntity;
import org.borispopic.paymenttransfersystem.entity.TransactionEntity;
import org.borispopic.paymenttransfersystem.enums.AccountType;
import org.borispopic.paymenttransfersystem.mapper.TransactionsMapper;
import org.borispopic.paymenttransfersystem.mapper.TransactionsMapperImpl;
import org.borispopic.paymenttransfersystem.repository.AccountRepository;
import org.borispopic.paymenttransfersystem.repository.LedgerRepository;
import org.borispopic.paymenttransfersystem.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private LedgerRepository ledgerRepository;

    private TransactionsMapper transactionsMapper;

    private TransactionServiceImpl transactionService;

    @Test
    void shouldReturnThreeTransactions() {

        transactionsMapper = new TransactionsMapperImpl();
        transactionService = new TransactionServiceImpl(accountRepository, transactionRepository, ledgerRepository, transactionsMapper);

        TransactionEntity testEntityOne = TransactionEntity.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(100.50))
                .comment("Test transaction one")
                .entryDate(LocalDate.now())
                .sourceAccountId("IBAN_TEST_ACCOUNT_001")
                .destinationAccountId("IBAN_TEST_ACCOUNT_002")
                .build();

        TransactionEntity testEntityTwo = TransactionEntity.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(10.05))
                .comment("Test transaction two")
                .entryDate(LocalDate.now())
                .sourceAccountId("IBAN_TEST_ACCOUNT_001")
                .destinationAccountId("IBAN_TEST_ACCOUNT_002")
                .build();

        TransactionEntity testEntityThree = TransactionEntity.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(92.65))
                .comment("Test transaction three")
                .entryDate(LocalDate.now())
                .sourceAccountId("IBAN_TEST_ACCOUNT_001")
                .destinationAccountId("IBAN_TEST_ACCOUNT_002")
                .build();

        List<TransactionEntity> testTransactions = List.of(testEntityOne,testEntityTwo,testEntityThree);

        Mockito.when(transactionRepository.findAll()).thenReturn(testTransactions);

        var allTransactions = transactionService.getAllTransactions();

        assertEquals(3, allTransactions.size());
        Mockito.verify(transactionRepository, Mockito.times(1)).findAll();
    }

    @Test
    void shouldPerformTransactionAndReturnSavedTransaction() {
        transactionsMapper = new TransactionsMapperImpl();
        transactionService = new TransactionServiceImpl(accountRepository, transactionRepository, ledgerRepository, transactionsMapper);

        Transaction performTestTransaction = Transaction.builder()
                .id(UUID.fromString("00000007-0007-0007-0007-000000000001"))
                .amount(BigDecimal.valueOf(100.50))
                .comment("Performing test transaction")
                .entryDate(LocalDate.now())
                .sourceAccountId("IBAN_TEST_ACCOUNT_002")
                .destinationAccountId("IBAN_TEST_ACCOUNT_003")
                .build();

        TransactionEntity storedTestTransaction = TransactionEntity.builder()
                .id(UUID.fromString("00000007-0007-0007-0007-000000000001"))
                .amount(BigDecimal.valueOf(100.50))
                .comment("Performing test transaction")
                .entryDate(LocalDate.now())
                .sourceAccountId("IBAN_TEST_ACCOUNT_002")
                .destinationAccountId("IBAN_TEST_ACCOUNT_003")
                .build();

        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(storedTestTransaction);

        AccountEntity testAccountEntyty = AccountEntity.builder()
                .accountNumber("IBAN_TEST_ACCOUNT_001")
                .owner("Boris Popic")
                .balance(BigDecimal.valueOf(100.50))
                .accountType(AccountType.PERSONAL)
                .registered(LocalDate.now())
                .currency("USD")
                .comment("a test account")
                .build();

        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(testAccountEntyty));
        Mockito.when(ledgerRepository.save(Mockito.any())).thenReturn(Mockito.any());

        var expectedOptional = transactionService.performTransaction(performTestTransaction);

        assertTrue(expectedOptional.isPresent());
        assertThat(expectedOptional.get()).isEqualTo(performTestTransaction);

        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(accountRepository, Mockito.times(4)).findById(Mockito.any());
        Mockito.verify(ledgerRepository, Mockito.times(2)).save(Mockito.any());
    }

    @Test
    void shoudlPerformValidateTransactionandReturnsEmptyOptional() {
        transactionsMapper = new TransactionsMapperImpl();
        transactionService = new TransactionServiceImpl(accountRepository, transactionRepository, ledgerRepository, transactionsMapper);

        Mockito.reset(transactionRepository, ledgerRepository, accountRepository);

        Mockito.when(accountRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        Transaction testTransaction = Transaction.builder()
                .sourceAccountId("IBAN_TEST_ACCOUNT_001")
                .amount(BigDecimal.valueOf(100.50))
                .comment("Test transaction")
                .destinationAccountId("IBAN_TEST_ACCOUNT_002")
                .build();

        AccountEntity testAccountEntity = AccountEntity.builder()
                .accountNumber("IBAN_TEST_ACCOUNT_001")
                .balance(BigDecimal.valueOf(90.00))
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .build();

        assertEquals(Optional.empty(), transactionService.performTransaction(testTransaction));
        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any());

        Mockito.reset(accountRepository);
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(testAccountEntity));

        assertEquals(Optional.empty(), transactionService.performTransaction(testTransaction));

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    void shoudlPerformValidateTransactionandReturnsEmptyOptionalWhenDestinationAccountNotFound() {
        transactionsMapper = new TransactionsMapperImpl();
        transactionService = new TransactionServiceImpl(accountRepository, transactionRepository, ledgerRepository, transactionsMapper);

        Mockito.reset(transactionRepository, ledgerRepository, accountRepository);

        AccountEntity testAccountEntity = AccountEntity.builder()
                .accountNumber("IBAN_TEST_ACCOUNT_001")
                .balance(BigDecimal.valueOf(100.50))
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .build();

        Mockito.when(accountRepository.findById("IBAN_TEST_ACCOUNT_001"))
                .thenReturn(Optional.of(testAccountEntity));

        Mockito.when(accountRepository.findById("IBAN_TEST_ACCOUNT_002"))
                .thenReturn(Optional.empty());

        Transaction testTransaction = Transaction.builder()
                .sourceAccountId("IBAN_TEST_ACCOUNT_001")
                .amount(BigDecimal.valueOf(100.50))
                .comment("Test transaction")
                .destinationAccountId("IBAN_TEST_ACCOUNT_002")
                .build();

        assertEquals(Optional.empty(), transactionService.performTransaction(testTransaction));
        Mockito.verify(accountRepository, Mockito.times(2)).findById(Mockito.any());
    }

    @Test
    void shouldReturnTransaction() {
        transactionsMapper = new TransactionsMapperImpl();
        transactionService = new TransactionServiceImpl(accountRepository, transactionRepository, ledgerRepository, transactionsMapper);

        Mockito.reset(transactionRepository);
        Mockito.reset(accountRepository);
        Mockito.reset(ledgerRepository);

        Transaction testTransaction = Transaction.builder()
                .id(UUID.fromString("00000007-0007-0007-0007-000000000001"))
                .amount(BigDecimal.valueOf(100.50))
                .comment("Performing test transaction")
                .entryDate(LocalDate.now())
                .sourceAccountId("IBAN_TEST_ACCOUNT_001")
                .destinationAccountId("IBAN_TEST_ACCOUNT_002")
                .build();

        TransactionEntity testTransactionEntity = TransactionEntity.builder()
                .id(UUID.fromString("00000007-0007-0007-0007-000000000001"))
                .amount(BigDecimal.valueOf(100.50))
                .comment("Performing test transaction")
                .entryDate(LocalDate.now())
                .sourceAccountId("IBAN_TEST_ACCOUNT_001")
                .destinationAccountId("IBAN_TEST_ACCOUNT_002")
                .build();

        Mockito.when(transactionRepository.findById(UUID.fromString("00000007-0007-0007-0007-000000000001"))).thenReturn(Optional.of(testTransactionEntity));

        var received = transactionService.getTransaction(UUID.fromString("00000007-0007-0007-0007-000000000001"));

        assertThat(received.isPresent()).isTrue();
        assertThat(received.get()).isEqualTo(testTransaction);

        Mockito.verify(transactionRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    void shouldReturnTransactions() {
        transactionsMapper = new TransactionsMapperImpl();
        transactionService = new TransactionServiceImpl(accountRepository, transactionRepository, ledgerRepository, transactionsMapper);

        Mockito.reset(transactionRepository);

        TransactionEntity testTransactionEntityOne = TransactionEntity.builder()
                .id(UUID.fromString("00000007-0007-0007-0007-000000000001"))
                .amount(BigDecimal.valueOf(100.51))
                .comment("Performing test transaction 1")
                .entryDate(LocalDate.now())
                .sourceAccountId("IBAN_TEST_ACCOUNT_001")
                .destinationAccountId("IBAN_TEST_ACCOUNT_002")
                .build();

        TransactionEntity testTransactionEntityTwo = TransactionEntity.builder()
                .id(UUID.fromString("00000007-0007-0007-0007-000000000002"))
                .amount(BigDecimal.valueOf(100.52))
                .comment("Performing test transaction 2")
                .entryDate(LocalDate.now())
                .sourceAccountId("IBAN_TEST_ACCOUNT_002")
                .destinationAccountId("IBAN_TEST_ACCOUNT_003")
                .build();

        List<TransactionEntity> testTransactions = List.of(testTransactionEntityOne, testTransactionEntityTwo);

        Mockito.when(transactionRepository.findAll()).thenReturn(testTransactions);

        var returnedTestTransactions = transactionService.getAllTransactions();

        assertThat(returnedTestTransactions.size()).isEqualTo(2);
    }

    @Test
    void shouldReturnTransactionsForAccount() {
        transactionsMapper = new TransactionsMapperImpl();
        transactionService = new TransactionServiceImpl(accountRepository, transactionRepository, ledgerRepository, transactionsMapper);

        Mockito.reset(transactionRepository);

        TransactionEntity testTransactionEntityOne = TransactionEntity.builder()
                .id(UUID.fromString("00000007-0007-0007-0007-000000000001"))
                .amount(BigDecimal.valueOf(100.51))
                .comment("Performing test transaction 1")
                .entryDate(LocalDate.now())
                .sourceAccountId("IBAN_TEST_ACCOUNT_001")
                .destinationAccountId("IBAN_TEST_ACCOUNT_002")
                .build();

        TransactionEntity testTransactionEntityTwo = TransactionEntity.builder()
                .id(UUID.fromString("00000007-0007-0007-0007-000000000002"))
                .amount(BigDecimal.valueOf(100.52))
                .comment("Performing test transaction 2")
                .entryDate(LocalDate.now())
                .sourceAccountId("IBAN_TEST_ACCOUNT_003")
                .destinationAccountId("IBAN_TEST_ACCOUNT_001")
                .build();

        Mockito.when(transactionRepository.findAll(Mockito.<Example<TransactionEntity>>any()))
                .thenReturn(List.of(testTransactionEntityOne))
                .thenReturn(List.of(testTransactionEntityTwo));


        var returnedTestTransactions = transactionService.getTransactionsForAccount("IBAN_TEST_ACCOUNT_001");

        assertThat(returnedTestTransactions.size()).isEqualTo(2);
    }
}
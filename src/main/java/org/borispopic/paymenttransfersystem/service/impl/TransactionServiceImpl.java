package org.borispopic.paymenttransfersystem.service.impl;

import jakarta.transaction.Transactional;
import org.borispopic.paymenttransfersystem.domain.Transaction;
import org.borispopic.paymenttransfersystem.entity.LedgerEntity;
import org.borispopic.paymenttransfersystem.entity.TransactionEntity;
import org.borispopic.paymenttransfersystem.enums.EntryType;
import org.borispopic.paymenttransfersystem.exception.InvalidTransactionParametersException;
import org.borispopic.paymenttransfersystem.mapper.TransactionsMapper;
import org.borispopic.paymenttransfersystem.repository.AccountRepository;
import org.borispopic.paymenttransfersystem.repository.LedgerRepository;
import org.borispopic.paymenttransfersystem.repository.TransactionRepository;
import org.borispopic.paymenttransfersystem.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final LedgerRepository ledgerRepository;
    private final TransactionsMapper transactionsMapper;

    @Autowired
    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, LedgerRepository ledgerRepository, TransactionsMapper transactionsMapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.ledgerRepository = ledgerRepository;

        this.transactionsMapper = transactionsMapper;
    }

    /*
        This is one of the core features required by the authors of the task/project.
        The feature is developed according to ACID principles.

     */
    @Transactional
    @Override
    public Optional<Transaction> performTransaction(Transaction transaction) {
        //validate the transaction request
        try {
            this.validateTransaction(transaction);
        } catch (InvalidTransactionParametersException e) {
            return Optional.empty();
        }

        var currentBalance = accountRepository.findById(transaction.getSourceAccountId()).get().getBalance();
        accountRepository.updateAccountBalanceNative(transaction.getSourceAccountId(), currentBalance.subtract(transaction.getAmount()));

        var currentBalanceDestination = accountRepository.findById(transaction.getDestinationAccountId()).get().getBalance();
        accountRepository.updateAccountBalanceNative(transaction.getDestinationAccountId(), currentBalance.add(transaction.getAmount()));

        transaction.setEntryDate(LocalDate.now());

        var registeredTransaction =transactionsMapper.mapTransactionEntityToTransaction(transactionRepository.save(transactionsMapper.mapTransactionToTransactionEntity(transaction)));

        //insert the corresponding transaction entry into transaction_entries, credit
        LedgerEntity credit = LedgerEntity.builder()
                .transactionId(registeredTransaction.getId())
                .type(EntryType.CREDIT)
                .amount(transaction.getAmount())
                .entryDate(LocalDate.now())
                .build();

        ledgerRepository.save(credit);

        //insert the corresponding transaction entry into transaction_entries, debit
        LedgerEntity debit = LedgerEntity.builder()
                .transactionId(registeredTransaction.getId())
                .type(EntryType.DEBIT)
                .amount(transaction.getAmount())
                .entryDate(LocalDate.now())
                .build();

        ledgerRepository.save(debit);

        return Optional.of(registeredTransaction);
    }

    @Override
    public Optional<Transaction> getTransaction(UUID transactionId) {
        var transactionEntity = transactionRepository.findById(transactionId);
        return transactionEntity.map(transactionsMapper::mapTransactionEntityToTransaction);
    }

    @Override
    public List<Transaction> getTransactionsForAccount(String accountId) {
        TransactionEntity probeSource = TransactionEntity.builder()
                .sourceAccountId(accountId)
                .build();

        TransactionEntity probeDestination = TransactionEntity.builder()
                .destinationAccountId(accountId)
                .build();

        Example<TransactionEntity> exampleSource = Example.of(probeSource);
        Example<TransactionEntity> exampleDestination = Example.of(probeDestination);

        var listWithSource = transactionRepository.findAll(exampleSource).stream()
                .map(transactionsMapper::mapTransactionEntityToTransaction)
                .toList();

        var listWithDestination = transactionRepository.findAll(exampleDestination).stream()
                .map(transactionsMapper::mapTransactionEntityToTransaction)
                .toList();

        List<Transaction> combined = Stream.concat(listWithSource.stream(), listWithDestination.stream())
                .collect(Collectors.toList());

        return combined;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionsMapper::mapTransactionEntityToTransaction)
                .toList();
    }

    private void validateTransaction(Transaction transaction) throws InvalidTransactionParametersException {
        var sourceAccount = accountRepository.findById(transaction.getSourceAccountId());

        if( sourceAccount.isEmpty() )
            throw new InvalidTransactionParametersException("Source account not found");

        if( sourceAccount.get().getBalance().compareTo(transaction.getAmount()) == -1 )
            throw new InvalidTransactionParametersException("Source account balance out of range");

        var destinationAccount = accountRepository.findById(transaction.getDestinationAccountId());

        if( destinationAccount.isEmpty() )
            throw new InvalidTransactionParametersException("Destination account not found");
    }
}

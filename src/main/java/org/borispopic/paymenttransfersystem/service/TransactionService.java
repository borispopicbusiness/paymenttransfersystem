package org.borispopic.paymenttransfersystem.service;

import org.borispopic.paymenttransfersystem.domain.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionService {
    Optional<Transaction> performTransaction(Transaction transaction);
    Optional<Transaction> getTransaction(UUID transactionId);
    List<Transaction> getTransactionsForAccount(String accountId);
    List<Transaction> getAllTransactions();
}

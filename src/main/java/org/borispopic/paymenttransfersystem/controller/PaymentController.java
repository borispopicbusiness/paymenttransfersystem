package org.borispopic.paymenttransfersystem.controller;

import org.borispopic.paymenttransfersystem.dto.TransactionRequestDTO;
import org.borispopic.paymenttransfersystem.dto.TransactionResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PaymentController {
    TransactionResponseDTO getTransaction(UUID transactionId);
    List<TransactionResponseDTO> getTransactionsForAccount(String accountId);
    List<TransactionResponseDTO> getAllTransactions();
    TransactionResponseDTO performTransaction(TransactionRequestDTO transaction);
}

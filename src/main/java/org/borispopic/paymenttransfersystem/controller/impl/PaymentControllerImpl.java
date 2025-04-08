package org.borispopic.paymenttransfersystem.controller.impl;

import org.borispopic.paymenttransfersystem.controller.PaymentController;
import org.borispopic.paymenttransfersystem.dto.TransactionRequestDTO;
import org.borispopic.paymenttransfersystem.dto.TransactionResponseDTO;
import org.borispopic.paymenttransfersystem.mapper.TransactionsMapper;
import org.borispopic.paymenttransfersystem.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentControllerImpl implements PaymentController {

    private final TransactionService transactionService;
    private final TransactionsMapper transactionsMapper;

    @Autowired
    public PaymentControllerImpl(TransactionService transactionService, TransactionsMapper transactionsMapper) {
        this.transactionService = transactionService;
        this.transactionsMapper = transactionsMapper;
    }

    @GetMapping("/{transactionId}")
    @Override
    public TransactionResponseDTO getTransaction(@PathVariable UUID transactionId) {
        return transactionService.getTransaction(transactionId).map(transactionsMapper::mapTransactionToTransactionResponseDTO).get();
    }

    @GetMapping("/all/{accountId}")
    @Override
    public List<TransactionResponseDTO> getTransactionsForAccount(@PathVariable String accountId) {
        return transactionService.getTransactionsForAccount(accountId)
                .stream()
                .map(transactionsMapper::mapTransactionToTransactionResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionService.getAllTransactions().stream()
                .map(transactionsMapper::mapTransactionToTransactionResponseDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/execute")
    @Override
    public TransactionResponseDTO performTransaction(@RequestBody TransactionRequestDTO transaction) {
        return transactionService.performTransaction(transactionsMapper.maptoTransaction(transaction))
                .map(transactionsMapper::mapTransactionToTransactionResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
    }
}

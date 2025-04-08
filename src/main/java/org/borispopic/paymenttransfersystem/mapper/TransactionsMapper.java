package org.borispopic.paymenttransfersystem.mapper;

import org.borispopic.paymenttransfersystem.domain.Transaction;
import org.borispopic.paymenttransfersystem.dto.TransactionRequestDTO;
import org.borispopic.paymenttransfersystem.dto.TransactionResponseDTO;
import org.borispopic.paymenttransfersystem.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionsMapper {
    TransactionEntity mapTransactionToTransactionEntity(Transaction transaction);
    Transaction mapTransactionEntityToTransaction(TransactionEntity transactionEntity);
    TransactionResponseDTO mapTransactionToTransactionResponseDTO(Transaction transaction);

    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "sourceAccountId", target = "sourceAccountId")
    @Mapping(source = "destinationAccountId", target = "destinationAccountId")
    @Mapping(source = "comment",  target = "comment")
    Transaction maptoTransaction(TransactionRequestDTO transactionRequestDTO);
}

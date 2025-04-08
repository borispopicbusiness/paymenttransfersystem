package org.borispopic.paymenttransfersystem.mapper;

import org.borispopic.paymenttransfersystem.domain.Transaction;
import org.borispopic.paymenttransfersystem.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionsMapper {
    TransactionEntity mapTransactionToTransactionEntity(Transaction transaction);
    Transaction mapTransactionEntityToTransaction(TransactionEntity transactionEntity);
}

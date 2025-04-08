package org.borispopic.paymenttransfersystem.mapper;

import org.borispopic.paymenttransfersystem.domain.Account;
import org.borispopic.paymenttransfersystem.dto.AccountDTO;
import org.borispopic.paymenttransfersystem.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountEntity mapToAccountEntity(Account account);
    Account mapToAccount(AccountEntity accountEntity);
    AccountDTO mapToAccountDTO(Account account);
    Account mapToAccount(AccountDTO accountDTO);
}

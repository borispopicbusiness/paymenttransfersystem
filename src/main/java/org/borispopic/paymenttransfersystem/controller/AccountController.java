package org.borispopic.paymenttransfersystem.controller;

import org.borispopic.paymenttransfersystem.dto.AccountDTO;

import java.util.List;

public interface AccountController {
    AccountDTO getAccount(String accountId);
    List<AccountDTO> getAccounts();
    AccountDTO createAccount(AccountDTO account);
    AccountDTO updateAccount(AccountDTO account);
    void deleteAccount(String accountId);
}

package org.borispopic.paymenttransfersystem.service;

import org.borispopic.paymenttransfersystem.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccount(String accountId);
    Optional<Account> createAccount(Account account);
    List<Account> getAccounts();
    Optional<Account> updateAccount(Account account);
    void deleteAccount(String accountId);
}

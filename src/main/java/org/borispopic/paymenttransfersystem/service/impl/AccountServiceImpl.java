package org.borispopic.paymenttransfersystem.service.impl;

import org.borispopic.paymenttransfersystem.domain.Account;
import org.borispopic.paymenttransfersystem.mapper.AccountMapper;
import org.borispopic.paymenttransfersystem.repository.AccountRepository;
import org.borispopic.paymenttransfersystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Optional<Account> getAccount(String accountId) {
        return accountRepository.findById(accountId).map(accountMapper::mapToAccount);
    }

    @Override
    public Optional<Account> createAccount(Account account) {
        return Optional.of(accountMapper.mapToAccount(accountRepository.save(accountMapper.mapToAccountEntity(account))));
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::mapToAccount)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Account> updateAccount(Account account) {
        var updatedAccountEntity = accountRepository.save(accountMapper.mapToAccountEntity(account));
        return Optional.of(accountMapper.mapToAccount(updatedAccountEntity));
    }

    @Override
    public void deleteAccount(String accountId) {
        accountRepository.deleteById(accountId);
    }
}

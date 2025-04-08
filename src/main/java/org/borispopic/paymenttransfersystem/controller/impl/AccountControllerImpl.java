package org.borispopic.paymenttransfersystem.controller.impl;

import org.borispopic.paymenttransfersystem.controller.AccountController;
import org.borispopic.paymenttransfersystem.dto.AccountDTO;
import org.borispopic.paymenttransfersystem.mapper.AccountMapper;
import org.borispopic.paymenttransfersystem.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountControllerImpl implements AccountController {

    private final AccountServiceImpl accountService;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountControllerImpl(AccountServiceImpl accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @Override
    @GetMapping("/{accountId}")
    public AccountDTO getAccount(@PathVariable String accountId) {
        return accountMapper.mapToAccountDTO(accountService.getAccount(accountId).get());
    }

    @GetMapping("/all")
    @Override
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts()
                .stream()
                .map(accountMapper::mapToAccountDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/create")
    @Override
    public AccountDTO createAccount(@RequestBody AccountDTO account) {
        return accountMapper.mapToAccountDTO(accountService.createAccount(accountMapper.mapToAccount(account)).get());
    }

    @PutMapping("/update")
    @Override
    public AccountDTO updateAccount(@RequestBody AccountDTO account) {
        return accountMapper.mapToAccountDTO(accountService.updateAccount(accountMapper.mapToAccount(account)).get());
    }

    @DeleteMapping("/delete/{accountId}")
    @Override
    public void deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
    }
}

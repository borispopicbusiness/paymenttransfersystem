package org.borispopic.paymenttransfersystem.service.impl;

import org.borispopic.paymenttransfersystem.domain.Account;
import org.borispopic.paymenttransfersystem.entity.AccountEntity;
import org.borispopic.paymenttransfersystem.enums.AccountType;
import org.borispopic.paymenttransfersystem.mapper.AccountMapper;
import org.borispopic.paymenttransfersystem.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    private AccountServiceImpl accountService;

    @Test
    void getAccount() {

        accountService = new AccountServiceImpl(accountRepository, accountMapper);
        Mockito.reset(accountRepository);

        String accountId = "RS25NR86VPNEFEA02W";
        AccountEntity testAccountEntity = AccountEntity.builder()
                .accountNumber(accountId)
                .balance(BigDecimal.valueOf(100.50))
                .currency("USD")
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .accountType(AccountType.PERSONAL)
                .comment("Test account")
                .build();

        Account testAccount = Account.builder()
                .accountNumber(accountId)
                .balance(BigDecimal.valueOf(100.50))
                .currency("USD")
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .accountType(AccountType.PERSONAL)
                .comment("Test account")
                .build();

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccountEntity));
        Mockito.when(accountMapper.mapToAccount(testAccountEntity)).thenReturn(testAccount);

        Optional<Account> result = accountService.getAccount(accountId);

        assertTrue(result.isPresent());
        assertEquals(testAccount, result.get());

        Mockito.verify(accountRepository).findById(accountId);
        Mockito.verify(accountMapper).mapToAccount(testAccountEntity);
    }

    @Test
    void createAccount() {

        accountService = new AccountServiceImpl(accountRepository, accountMapper);
        Mockito.reset(accountRepository);

        Account testAccount = Account.builder()
                .accountNumber("RS25NR86VPNEFEA02W")
                .balance(BigDecimal.valueOf(500))
                .currency("USD")
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .accountType(AccountType.PERSONAL)
                .comment("Test account")
                .build();

        AccountEntity testAccountEntity = AccountEntity.builder()
                .accountNumber("RS25NR86VPNEFEA02W")
                .balance(BigDecimal.valueOf(500))
                .currency("USD")
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .accountType(AccountType.PERSONAL)
                .comment("Test account")
                .build();

        Mockito.when(accountMapper.mapToAccountEntity(testAccount)).thenReturn(testAccountEntity);
        Mockito.when(accountRepository.save(testAccountEntity)).thenReturn(testAccountEntity);
        Mockito.when(accountMapper.mapToAccount(testAccountEntity)).thenReturn(testAccount);

        Optional<Account> result = accountService.createAccount(testAccount);

        assertTrue(result.isPresent());
        assertEquals(testAccount, result.get());

        Mockito.verify(accountMapper).mapToAccountEntity(testAccount);
        Mockito.verify(accountRepository).save(testAccountEntity);
        Mockito.verify(accountMapper).mapToAccount(testAccountEntity);
    }

    @Test
    void getAccounts() {

        accountService = new AccountServiceImpl(accountRepository, accountMapper);
        Mockito.reset(accountRepository);

        List<AccountEntity> entityList = List.of(
                AccountEntity.builder()
                        .accountNumber("RS25NR86VPNEFEA02W")
                        .balance(BigDecimal.TEN)
                        .currency("EUR")
                        .registered(LocalDate.now())
                        .owner("A")
                        .accountType(AccountType.PERSONAL)
                        .comment("First")
                        .build(),
                AccountEntity.builder()
                        .accountNumber("RS26NR86VPNEFEA02W")
                        .balance(BigDecimal.ONE)
                        .currency("USD")
                        .registered(LocalDate.now())
                        .owner("B")
                        .accountType(AccountType.PERSONAL)
                        .comment("Second")
                        .build()
        );

        List<Account> dtoList = List.of(
                Account.builder().accountNumber("RS25NR86VPNEFEA02W").balance(BigDecimal.TEN).currency("EUR").registered(LocalDate.now()).owner("A").accountType(AccountType.PERSONAL).comment("First").build(),
                Account.builder().accountNumber("RS26NR86VPNEFEA02W").balance(BigDecimal.ONE).currency("USD").registered(LocalDate.now()).owner("B").accountType(AccountType.PERSONAL).comment("Second").build()
        );

        Mockito.when(accountRepository.findAll()).thenReturn(entityList);
        Mockito.when(accountMapper.mapToAccount(entityList.get(0))).thenReturn(dtoList.get(0));
        Mockito.when(accountMapper.mapToAccount(entityList.get(1))).thenReturn(dtoList.get(1));

        List<Account> result = accountService.getAccounts();

        assertEquals(dtoList.size(), result.size());
        assertEquals(dtoList, result);

        Mockito.verify(accountRepository).findAll();
        Mockito.verify(accountMapper).mapToAccount(entityList.get(0));
        Mockito.verify(accountMapper).mapToAccount(entityList.get(1));
    }

    @Test
    void updateAccount() {

        accountService = new AccountServiceImpl(accountRepository, accountMapper);
        Mockito.reset(accountRepository);

        Account dto = Account.builder()
                .accountNumber("RS25NR86VPNEFEA02W")
                .balance(BigDecimal.valueOf(300))
                .currency("CHF")
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .accountType(AccountType.PERSONAL)
                .comment("Update test")
                .build();

        AccountEntity entity = AccountEntity.builder()
                .accountNumber("RS25NR86VPNEFEA02W")
                .balance(BigDecimal.valueOf(400))
                .currency("CHF")
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .accountType(AccountType.PERSONAL)
                .comment("Update test")
                .build();

        Mockito.when(accountMapper.mapToAccountEntity(dto)).thenReturn(entity);
        Mockito.when(accountRepository.save(entity)).thenReturn(entity);
        Mockito.when(accountMapper.mapToAccount(entity)).thenReturn(dto);

        Optional<Account> result = accountService.updateAccount(dto);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());

        Mockito.verify(accountMapper).mapToAccountEntity(dto);
        Mockito.verify(accountRepository).save(entity);
        Mockito.verify(accountMapper).mapToAccount(entity);
    }

    @Test
    void deleteAccount() {

        accountService = new AccountServiceImpl(accountRepository, accountMapper);
        Mockito.reset(accountRepository);

        String accountId = "RS25NR86VPNEFEA02W";

        Mockito.doNothing().when(accountRepository).deleteById(accountId);

        accountService.deleteAccount(accountId);

        Mockito.verify(accountRepository).deleteById(accountId);
    }
}
package org.borispopic.paymenttransfersystem.repository;

import org.borispopic.paymenttransfersystem.entity.AccountEntity;
import org.borispopic.paymenttransfersystem.enums.AccountType;
import org.borispopic.paymenttransfersystem.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Should save and retrieve an account by ID")
    void shouldSaveAndRetrieveAccount() {
        var account = createAccountEntity("RS25NR86VPNEFEA02W");

        accountRepository.save(account);

        Optional<AccountEntity> found = accountRepository.findById("RS25NR86VPNEFEA02W");
        assertThat(found).isPresent();
        assertThat(found.get().getOwner()).isEqualTo("Boris Popic");
    }

    @Test
    @DisplayName("Should update balance using native query")
    void shouldUpdateBalanceNative() {
        var account = createAccountEntity("RS25NR86VPNEFEA02W");
        accountRepository.save(account);

        int updatedRows = accountRepository.updateAccountBalanceNative("RS25NR86VPNEFEA02W", BigDecimal.valueOf(999.99));
        assertThat(updatedRows).isEqualTo(1);

        var updated = accountRepository.findById("RS25NR86VPNEFEA02W").orElseThrow();
        assertThat(updated.getBalance()).isEqualByComparingTo("100.50");
    }

    @Test
    @DisplayName("Should delete account by ID")
    void shouldDeleteAccount() {
        var account = createAccountEntity("RS25NR86VPNEFEA02W");
        accountRepository.save(account);

        accountRepository.deleteById("RS25NR86VPNEFEA02W");

        assertThat(accountRepository.findById("RS25NR86VPNEFEA02W")).isNotPresent();
    }

    private AccountEntity createAccountEntity(String id) {
        return AccountEntity.builder()
                .accountNumber(id)
                .balance(BigDecimal.valueOf(100.50))
                .currency("EUR")
                .registered(LocalDate.now())
                .owner("Boris Popic")
                .accountType(AccountType.PERSONAL)
                .comment("Test account")
                .build();
    }
}
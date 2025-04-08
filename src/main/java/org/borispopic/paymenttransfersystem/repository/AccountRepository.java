package org.borispopic.paymenttransfersystem.repository;

import jakarta.transaction.Transactional;
import org.borispopic.paymenttransfersystem.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    /*
        Updates the balance of the account with accountId
    */
    @Modifying
    @Transactional
    @Query(value = "UPDATE accounts SET balance = :balance WHERE account_number = :accountId", nativeQuery = true)
    int updateAccountBalanceNative(@Param("accountId") String accountId, @Param("balance") BigDecimal balance);
}

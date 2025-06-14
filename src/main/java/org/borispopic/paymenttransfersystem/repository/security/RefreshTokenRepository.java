package org.borispopic.paymenttransfersystem.repository.security;

import org.borispopic.paymenttransfersystem.entity.security.RefreshTokenEntity;
import org.borispopic.paymenttransfersystem.entity.security.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    @Query("SELECT r FROM RefreshTokenEntity r WHERE r.token = ?1")
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUser(UserEntity user);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.token <= ?1")
    void deleteByToken(String token);
}
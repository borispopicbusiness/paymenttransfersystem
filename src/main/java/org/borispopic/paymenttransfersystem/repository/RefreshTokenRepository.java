package org.borispopic.paymenttransfersystem.repository;

import org.borispopic.paymenttransfersystem.entity.RefreshToken;
import org.borispopic.paymenttransfersystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
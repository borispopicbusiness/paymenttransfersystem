package org.borispopic.paymenttransfersystem.service.impl.security;

import org.borispopic.paymenttransfersystem.entity.security.RefreshTokenEntity;
import org.borispopic.paymenttransfersystem.entity.security.UserEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.borispopic.paymenttransfersystem.repository.security.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    @Lazy
    @Autowired
    private RefreshTokenService selfProxy;

    public RefreshTokenEntity createRefreshToken(UserEntity user) {
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            //refreshTokenRepository.deleteByToken(token.getToken());
            //refreshTokenRepository.flush();
            deleteToken(token.getToken());
            return null;

            //throw new RuntimeException("Refresh token was expired");
        }
        return token;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void deleteToken(String token) {
        refreshTokenRepository.deleteByToken(token);
        refreshTokenRepository.flush();
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

}
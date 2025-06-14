package org.borispopic.paymenttransfersystem.controller.impl;

import lombok.RequiredArgsConstructor;
import org.borispopic.paymenttransfersystem.controller.AuthController;
import org.borispopic.paymenttransfersystem.dto.security.AuthenticationRequestDTO;
import org.borispopic.paymenttransfersystem.dto.security.AuthenticationResponseDTO;
import org.borispopic.paymenttransfersystem.dto.security.RegisterRequestDTO;
import org.borispopic.paymenttransfersystem.dto.security.TokenRefreshRequestDTO;
import org.borispopic.paymenttransfersystem.dto.security.TokenRefreshResponseDTO;
import org.borispopic.paymenttransfersystem.entity.security.RefreshTokenEntity;
import org.borispopic.paymenttransfersystem.entity.security.UserEntity;
import org.borispopic.paymenttransfersystem.security.JwtUtils;
import org.borispopic.paymenttransfersystem.service.impl.security.RefreshTokenService;
import org.borispopic.paymenttransfersystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Override
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        UserEntity user = userService.register(request);
        String jwt = jwtUtils.generateToken(user);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);
        
        return ResponseEntity.ok(AuthenticationResponseDTO.builder()
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .build());
    }

    @Override
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserEntity user = userService.findByUsername(request.getUsername());
        String jwt = jwtUtils.generateToken(user);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(AuthenticationResponseDTO.builder()
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .build());
    }

    @Override
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponseDTO> refreshToken(@RequestBody TokenRefreshRequestDTO request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUser)
                .map(user -> {
                    String token = jwtUtils.generateToken(user);
                    return ResponseEntity.ok(new TokenRefreshResponseDTO(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}
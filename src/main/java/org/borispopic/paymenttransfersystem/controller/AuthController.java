package org.borispopic.paymenttransfersystem.controller;

import org.borispopic.paymenttransfersystem.dto.security.AuthenticationRequestDTO;
import org.borispopic.paymenttransfersystem.dto.security.AuthenticationResponseDTO;
import org.borispopic.paymenttransfersystem.dto.security.RegisterRequestDTO;
import org.borispopic.paymenttransfersystem.dto.security.TokenRefreshRequestDTO;
import org.borispopic.paymenttransfersystem.dto.security.TokenRefreshResponseDTO;
import org.springframework.http.ResponseEntity;

public interface AuthController {
    ResponseEntity<AuthenticationResponseDTO> register(RegisterRequestDTO request);
    ResponseEntity<AuthenticationResponseDTO> authenticate(AuthenticationRequestDTO request);
    ResponseEntity<TokenRefreshResponseDTO> refreshToken(TokenRefreshRequestDTO request);
}

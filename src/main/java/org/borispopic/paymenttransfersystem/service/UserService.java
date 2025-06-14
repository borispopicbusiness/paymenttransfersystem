package org.borispopic.paymenttransfersystem.service;

import org.borispopic.paymenttransfersystem.dto.security.RegisterRequestDTO;
import org.borispopic.paymenttransfersystem.entity.security.UserEntity;

public interface UserService {
    UserEntity register(RegisterRequestDTO request);
    UserEntity findByUsername(String username);
}
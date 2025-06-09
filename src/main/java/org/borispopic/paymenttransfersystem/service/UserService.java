package org.borispopic.paymenttransfersystem.service;

import org.borispopic.paymenttransfersystem.dto.RegisterRequest;
import org.borispopic.paymenttransfersystem.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(RegisterRequest request);
    User findByUsername(String username);
}
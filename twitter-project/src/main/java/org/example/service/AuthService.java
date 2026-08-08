package org.example.service;

import org.example.dto.auth.AuthResponse;
import org.example.dto.auth.LoginRequest;
import org.example.dto.auth.RegisterRequest;
import org.example.entity.User;

public interface AuthService {

    String register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    Long getUserIdByUsername(String username);

    User getUserByUsername(String username);
}
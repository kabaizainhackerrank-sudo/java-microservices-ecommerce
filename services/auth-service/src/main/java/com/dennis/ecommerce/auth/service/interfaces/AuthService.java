package com.dennis.ecommerce.auth.service.interfaces;

import com.dennis.ecommerce.auth.domain.entity.User;
import com.dennis.ecommerce.auth.dto.AuthResponse;
import com.dennis.ecommerce.auth.dto.LoginRequest;
import com.dennis.ecommerce.auth.dto.RefreshTokenRequest;
import com.dennis.ecommerce.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshTokenRequest request);
    User getMe(String userId);
}

package com.dennis.ecommerce.auth.controller;

import com.dennis.ecommerce.auth.domain.entity.User;
import com.dennis.ecommerce.auth.dto.*;
import com.dennis.ecommerce.auth.service.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController // combina @Controller + @ResponseBody, todo lo que retornan los métodos se serializa a JSON automáticamente
@RequestMapping("/auth") // todos los endpoints de este controller empiezan con /auth
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        User user = authService.getMe(userId);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }
}
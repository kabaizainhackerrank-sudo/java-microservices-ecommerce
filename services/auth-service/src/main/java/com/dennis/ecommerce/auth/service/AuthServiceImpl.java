package com.dennis.ecommerce.auth.service;

import com.dennis.ecommerce.auth.domain.entity.RefreshToken;
import com.dennis.ecommerce.auth.domain.entity.User;
import com.dennis.ecommerce.auth.dto.AuthResponse;
import com.dennis.ecommerce.auth.dto.LoginRequest;
import com.dennis.ecommerce.auth.dto.RefreshTokenRequest;
import com.dennis.ecommerce.auth.dto.RegisterRequest;
import com.dennis.ecommerce.auth.event.UserCreatedEvent;
import com.dennis.ecommerce.auth.event.UserEventPublisher;
import com.dennis.ecommerce.auth.exception.AuthException;
import com.dennis.ecommerce.auth.repository.RefreshTokenRepository;
import com.dennis.ecommerce.auth.repository.UserRepository;
import com.dennis.ecommerce.auth.security.JwtTokenProvider;
import com.dennis.ecommerce.auth.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserEventPublisher userEventPublisher;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // Validar que el email no exista
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("El email ya está registrado", HttpStatus.CONFLICT);
        }

        // Crear el usuario
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
                .build();

        userRepository.save(user);

        // Publicar evento
        userEventPublisher.publishUserCreated(
                new UserCreatedEvent(user.getId(), user.getEmail(), user.getRole().name())
        );

        return buildAuthResponse(user);

     /*   // Generar token
        String token = jwtTokenProvider.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getRole().name());*/
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {

        // Buscar usuario
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Credenciales inválidas", HttpStatus.UNAUTHORIZED));

        // Validar que esté activo
        if (!user.isActive()) {
            throw new AuthException("Usuario bloqueado", HttpStatus.FORBIDDEN);
        }

        // Validar password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }

        // Generar token
        String token = jwtTokenProvider.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new AuthException("Refresh token inválido", HttpStatus.UNAUTHORIZED));

        if (refreshToken.isRevoked()) {
            throw new AuthException("Refresh token revocado", HttpStatus.UNAUTHORIZED);
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AuthException("Refresh token expirado", HttpStatus.UNAUTHORIZED);
        }

        User user = refreshToken.getUser();

        // Revocar el token usado
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        // Emitir nueva respuesta con nuevo token
        return buildAuthResponse(user);
    }

    @Override
    public User getMe(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("Usuario no encontrado", HttpStatus.NOT_FOUND));
    }

    @Transactional
    private AuthResponse buildAuthResponse(User user) {
        String token = jwtTokenProvider.generateToken(
                user.getId(), user.getEmail(), user.getRole().name()
        );

        // Revocar tokens anteriores y crear uno nuevo
        refreshTokenRepository.revokeAllByUserId(user.getId());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
                token,
                refreshToken.getToken(),
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}

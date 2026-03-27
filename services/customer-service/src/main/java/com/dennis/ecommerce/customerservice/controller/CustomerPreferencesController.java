package com.dennis.ecommerce.customerservice.controller;

import com.dennis.ecommerce.customerservice.dto.request.CustomerPreferencesRequest;
import com.dennis.ecommerce.customerservice.dto.response.CustomerPreferencesResponse;
import com.dennis.ecommerce.customerservice.service.interfaces.CustomerPreferencesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/me/preferences")
@RequiredArgsConstructor
public class CustomerPreferencesController {

    private final CustomerPreferencesService preferencesService;

    // ── Ver preferencias ──
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerPreferencesResponse> getPreferences(
            Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(preferencesService.getPreferences(userId));
    }

    // ── Actualizar preferencias ──
    @PutMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerPreferencesResponse> updatePreferences(
            Authentication authentication,
            @Valid @RequestBody CustomerPreferencesRequest request) {

        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(preferencesService.updatePreferences(userId, request));
    }
}

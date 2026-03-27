package com.dennis.ecommerce.customerservice.controller;

import com.dennis.ecommerce.customerservice.dto.request.ChangeCustomerStatusRequest;
import com.dennis.ecommerce.customerservice.dto.request.UpdateCustomerRequest;
import com.dennis.ecommerce.customerservice.dto.response.CustomerResponse;
import com.dennis.ecommerce.customerservice.dto.response.CustomerStatusHistoryResponse;
import com.dennis.ecommerce.customerservice.service.interfaces.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // ── Perfil del cliente autenticado ──
    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')") //protege el endpoint a nivel de método. Si el token no tiene ese rol, Spring devuelve 403 antes de entrar al método.
    public ResponseEntity<CustomerResponse> getMyProfile(
            Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(customerService.getByUserId(userId));
    }

    // ── Actualizar perfil ──
    @PutMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")//protege el endpoint a nivel de método. Si el token no tiene ese rol, Spring devuelve 403 antes de entrar al método.
    public ResponseEntity<CustomerResponse> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateCustomerRequest request) {

        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(customerService.updateCustomer(userId, request));
    }

    // ── Consulta por customerId (admin o servicios internos) ──
    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")//protege el endpoint a nivel de método. Si el token no tiene ese rol, Spring devuelve 403 antes de entrar al método.
    public ResponseEntity<CustomerResponse> getByCustomerId(
            @PathVariable UUID customerId) {

        return ResponseEntity.ok(customerService.getByCustomerId(customerId));
    }

    // ── Cambiar estado del cliente (solo admin) ──
    @PatchMapping("/{customerId}/status")
    @PreAuthorize("hasRole('ADMIN')")//protege el endpoint a nivel de método. Si el token no tiene ese rol, Spring devuelve 403 antes de entrar al método.
    public ResponseEntity<CustomerResponse> changeStatus(
            @PathVariable UUID customerId,
            Authentication authentication,
            @Valid @RequestBody ChangeCustomerStatusRequest request) {

        UUID adminUserId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(customerService.changeStatus(customerId, adminUserId, request));
    }

    // ── Historial de estados (solo admin) ──
    @GetMapping("/{customerId}/status-history")
    @PreAuthorize("hasRole('ADMIN')")//protege el endpoint a nivel de método. Si el token no tiene ese rol, Spring devuelve 403 antes de entrar al método.
    public ResponseEntity<List<CustomerStatusHistoryResponse>> getStatusHistory(
            @PathVariable UUID customerId) {

        return ResponseEntity.ok(customerService.getStatusHistory(customerId));
    }
}

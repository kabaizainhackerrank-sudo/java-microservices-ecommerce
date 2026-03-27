package com.dennis.ecommerce.customerservice.controller;

import com.dennis.ecommerce.customerservice.dto.request.AddressRequest;
import com.dennis.ecommerce.customerservice.dto.response.AddressResponse;
import com.dennis.ecommerce.customerservice.service.interfaces.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/me/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // ── Listar direcciones ──
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<AddressResponse>> getAddresses(
            Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(addressService.getAddresses(userId));
    }

    // ── Agregar dirección ──
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<AddressResponse> addAddress(
            Authentication authentication,
            @Valid @RequestBody AddressRequest request) {

        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.addAddress(userId, request));
    }

    // ── Actualizar dirección ──
    @PutMapping("/{addressId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<AddressResponse> updateAddress(
            Authentication authentication,
            @PathVariable UUID addressId,
            @Valid @RequestBody AddressRequest request) {

        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(addressService.updateAddress(userId, addressId, request));
    }

    // ── Eliminar dirección ──
    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> deleteAddress(
            Authentication authentication,
            @PathVariable UUID addressId) {

        UUID userId = UUID.fromString(authentication.getName());
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();//el DELETE devuelve 204 No Content, que es el estándar HTTP para
                                                  //operaciones de eliminación exitosas sin cuerpo de respuesta
    }

    // ── Marcar como predeterminada ──
    @PatchMapping("/{addressId}/default")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<AddressResponse> setDefault(
            Authentication authentication,
            @PathVariable UUID addressId) {

        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(addressService.setDefaultAddress(userId, addressId));
    }
}

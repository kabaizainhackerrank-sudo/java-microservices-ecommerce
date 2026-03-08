package com.dennis.ecommerce.auth.dto;

import com.dennis.ecommerce.auth.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @Email(message = "Email inválido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "El password es obligatorio")
    @Size(min = 6, message = "El password debe tener al menos 6 caracteres")
    private String password;

    private Role role = Role.CUSTOMER; // por defecto CUSTOMER
}

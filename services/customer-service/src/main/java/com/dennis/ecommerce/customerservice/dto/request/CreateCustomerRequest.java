package com.dennis.ecommerce.customerservice.dto.request;

import com.dennis.ecommerce.customerservice.domain.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {

    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String firstName;

    @Size(max = 100, message = "El apellido no puede superar 100 caracteres")
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Formato de teléfono inválido")
    private String phone;

    private DocumentType documentType;

    @Size(max = 50, message = "El número de documento no puede superar 50 caracteres")
    private String documentNumber;
}

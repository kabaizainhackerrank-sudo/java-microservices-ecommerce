package com.dennis.ecommerce.customerservice.dto.request;

import com.dennis.ecommerce.customerservice.domain.enums.DocumentType;
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
public class UpdateCustomerRequest {

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Formato de teléfono inválido")
    private String phone;

    private DocumentType documentType;

    @Size(max = 50)
    private String documentNumber;
}
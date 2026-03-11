package com.dennis.ecommerce.customerservice.dto.request;

import com.dennis.ecommerce.customerservice.domain.enums.CustomerStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCustomerStatusRequest {

    @NotNull(message = "El estado es obligatorio")
    private CustomerStatus status;

    @Size(max = 500, message = "La razón no puede superar 500 caracteres")
    private String reason;
}

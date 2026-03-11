package com.dennis.ecommerce.customerservice.dto.request;

import com.dennis.ecommerce.customerservice.domain.enums.AddressLabel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
////// @Data Es exactamente igual a escribir:
/// @Getter
/// @Setter
/// @ToString
/// @EqualsAndHashCode
/// @RequiredArgsConstructor
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    @NotNull(message = "La etiqueta es obligatoria")
    private AddressLabel label;

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 200)
    private String street;

    @Size(max = 20)
    private String number;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @NotBlank(message = "El país es obligatorio")
    @Size(max = 100)
    private String country;

    @Size(max = 20)
    private String zipCode;

    private Boolean isDefault = false;
}

package com.dennis.ecommerce.customerservice.dto.response;

import com.dennis.ecommerce.customerservice.domain.enums.AddressLabel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private UUID addressId;
    private AddressLabel label;
    private String street;
    private String number;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
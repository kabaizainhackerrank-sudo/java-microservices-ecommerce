package com.dennis.ecommerce.customerservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPreferencesResponse {

    private UUID preferenceId;
    private String language;
    private String currency;
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean pushNotifications;
}

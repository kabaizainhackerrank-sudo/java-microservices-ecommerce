package com.dennis.ecommerce.customerservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPreferencesRequest {

    private String language;
    private String currency;
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean pushNotifications;
}

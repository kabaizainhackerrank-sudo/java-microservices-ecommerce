package com.dennis.ecommerce.customerservice.service.interfaces;

import com.dennis.ecommerce.customerservice.dto.request.CustomerPreferencesRequest;
import com.dennis.ecommerce.customerservice.dto.response.CustomerPreferencesResponse;

import java.util.UUID;

public interface CustomerPreferencesService {
    CustomerPreferencesResponse getPreferences(UUID userId);
    CustomerPreferencesResponse updatePreferences(UUID userId, CustomerPreferencesRequest request);
}

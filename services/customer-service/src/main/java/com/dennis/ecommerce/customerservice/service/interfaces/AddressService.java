package com.dennis.ecommerce.customerservice.service.interfaces;

import com.dennis.ecommerce.customerservice.dto.request.AddressRequest;
import com.dennis.ecommerce.customerservice.dto.response.AddressResponse;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    List<AddressResponse> getAddresses(UUID userId);
    AddressResponse addAddress(UUID userId, AddressRequest request);
    AddressResponse updateAddress(UUID userId, UUID addressId, AddressRequest request);
    void deleteAddress(UUID userId, UUID addressId);
    AddressResponse setDefaultAddress(UUID userId, UUID addressId);
}

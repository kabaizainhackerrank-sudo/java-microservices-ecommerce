package com.dennis.ecommerce.customerservice.service.interfaces;


import com.dennis.ecommerce.customerservice.domain.entity.Customer;
import com.dennis.ecommerce.customerservice.dto.request.ChangeCustomerStatusRequest;
import com.dennis.ecommerce.customerservice.dto.request.CreateCustomerRequest;
import com.dennis.ecommerce.customerservice.dto.request.UpdateCustomerRequest;
import com.dennis.ecommerce.customerservice.dto.response.CustomerResponse;
import com.dennis.ecommerce.customerservice.dto.response.CustomerStatusHistoryResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(UUID userId, CreateCustomerRequest request);
    CustomerResponse getByUserId(UUID userId);
    CustomerResponse getByCustomerId(UUID customerId);
    CustomerResponse updateCustomer(UUID userId, UpdateCustomerRequest request);
    CustomerResponse changeStatus(UUID customerId, UUID adminUserId, ChangeCustomerStatusRequest request);
    List<CustomerStatusHistoryResponse> getStatusHistory(UUID customerId);
    Customer findByUserIdOrThrow(UUID userId);
    Customer findByCustomerIdOrThrow(UUID customerId);
}
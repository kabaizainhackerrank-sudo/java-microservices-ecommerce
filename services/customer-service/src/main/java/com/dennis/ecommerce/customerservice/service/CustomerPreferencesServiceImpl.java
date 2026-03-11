package com.dennis.ecommerce.customerservice.service;

import com.dennis.ecommerce.customerservice.domain.entity.Customer;
import com.dennis.ecommerce.customerservice.domain.entity.CustomerPreferences;
import com.dennis.ecommerce.customerservice.dto.request.CustomerPreferencesRequest;
import com.dennis.ecommerce.customerservice.dto.response.CustomerPreferencesResponse;
import com.dennis.ecommerce.customerservice.mapper.CustomerPreferencesMapper;
import com.dennis.ecommerce.customerservice.repository.CustomerPreferencesRepository;
import com.dennis.ecommerce.customerservice.service.interfaces.CustomerPreferencesService;
import com.dennis.ecommerce.customerservice.service.interfaces.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerPreferencesServiceImpl implements CustomerPreferencesService {

    private final CustomerPreferencesRepository preferencesRepository;
    private final CustomerPreferencesMapper preferencesMapper;
    private final CustomerService customerService; // ← interfaz, no impl

    @Override
    @Transactional(readOnly = true)
    public CustomerPreferencesResponse getPreferences(UUID userId) {
        Customer customer = customerService.findByUserIdOrThrow(userId);
        return preferencesMapper.toResponse(findOrCreatePreferences(customer));
    }

    @Override
    @Transactional
    public CustomerPreferencesResponse updatePreferences(UUID userId, CustomerPreferencesRequest request) {
        Customer customer = customerService.findByUserIdOrThrow(userId);
        CustomerPreferences preferences = findOrCreatePreferences(customer);

        preferencesMapper.updateEntityFromRequest(request, preferences);
        CustomerPreferences updated = preferencesRepository.save(preferences);

        log.info("Preferencias actualizadas para customer: {}", customer.getCustomerId());
        return preferencesMapper.toResponse(updated);
    }

    private CustomerPreferences findOrCreatePreferences(Customer customer) {
        return preferencesRepository
                .findByCustomer_CustomerId(customer.getCustomerId())
                .orElseGet(() -> preferencesRepository.save(
                        CustomerPreferences.builder()
                                .customer(customer)
                                .build()
                ));
    }
}

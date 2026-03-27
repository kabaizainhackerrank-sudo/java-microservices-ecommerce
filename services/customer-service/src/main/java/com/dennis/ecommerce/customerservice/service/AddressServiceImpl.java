package com.dennis.ecommerce.customerservice.service;

import com.dennis.ecommerce.customerservice.domain.entity.Address;
import com.dennis.ecommerce.customerservice.domain.entity.Customer;
import com.dennis.ecommerce.customerservice.dto.request.AddressRequest;
import com.dennis.ecommerce.customerservice.dto.response.AddressResponse;
import com.dennis.ecommerce.customerservice.exception.AddressNotFoundException;
import com.dennis.ecommerce.customerservice.mapper.AddressMapper;
import com.dennis.ecommerce.customerservice.repository.AddressRepository;
import com.dennis.ecommerce.customerservice.service.interfaces.AddressService;
import com.dennis.ecommerce.customerservice.service.interfaces.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CustomerService customerService; // ← interfaz, no impl

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(UUID userId) {
        Customer customer = customerService.findByUserIdOrThrow(userId);
        return addressRepository.findByCustomer_CustomerId(customer.getCustomerId())
                .stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AddressResponse addAddress(UUID userId, AddressRequest request) {
        Customer customer = customerService.findByUserIdOrThrow(userId);

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.clearDefaultByCustomerId(customer.getCustomerId());
        }

        Address address = addressMapper.toEntity(request);
        address.setCustomer(customer);

        Address saved = addressRepository.save(address);
        log.info("Dirección agregada: {} para customer: {}", saved.getAddressId(), customer.getCustomerId());

        return addressMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(UUID userId, UUID addressId, AddressRequest request) {
        Customer customer = customerService.findByUserIdOrThrow(userId);
        Address address = findAddressOrThrow(addressId, customer.getCustomerId());

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.clearDefaultByCustomerId(customer.getCustomerId());
        }

        addressMapper.updateEntityFromRequest(request, address);
        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public void deleteAddress(UUID userId, UUID addressId) {
        Customer customer = customerService.findByUserIdOrThrow(userId);
        Address address = findAddressOrThrow(addressId, customer.getCustomerId());
        addressRepository.delete(address);
        log.info("Dirección eliminada: {}", addressId);
    }

    @Override
    @Transactional
    public AddressResponse setDefaultAddress(UUID userId, UUID addressId) {
        Customer customer = customerService.findByUserIdOrThrow(userId);
        Address address = findAddressOrThrow(addressId, customer.getCustomerId());

        addressRepository.clearDefaultByCustomerId(customer.getCustomerId());
        address.setIsDefault(true);

        log.info("Dirección {} marcada como default para customer: {}", addressId, customer.getCustomerId());
        return addressMapper.toResponse(addressRepository.save(address));
    }

    private Address findAddressOrThrow(UUID addressId, UUID customerId) {
        return addressRepository.findByAddressIdAndCustomer_CustomerId(addressId, customerId)
                .orElseThrow(() -> new AddressNotFoundException(
                        "Dirección no encontrada: " + addressId));
    }
}

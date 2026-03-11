package com.dennis.ecommerce.customerservice.service;

import com.dennis.ecommerce.customerservice.domain.entity.Customer;
import com.dennis.ecommerce.customerservice.domain.entity.CustomerStatusHistory;
import com.dennis.ecommerce.customerservice.domain.enums.CustomerStatus;
import com.dennis.ecommerce.customerservice.dto.request.ChangeCustomerStatusRequest;
import com.dennis.ecommerce.customerservice.dto.request.CreateCustomerRequest;
import com.dennis.ecommerce.customerservice.dto.request.UpdateCustomerRequest;
import com.dennis.ecommerce.customerservice.dto.response.CustomerResponse;
import com.dennis.ecommerce.customerservice.dto.response.CustomerStatusHistoryResponse;
import com.dennis.ecommerce.customerservice.exception.CustomerAlreadyExistsException;
import com.dennis.ecommerce.customerservice.exception.CustomerNotFoundException;
import com.dennis.ecommerce.customerservice.mapper.CustomerMapper;
import com.dennis.ecommerce.customerservice.mapper.CustomerStatusHistoryMapper;
import com.dennis.ecommerce.customerservice.messaging.publisher.CustomerStatusChangedPublisher;
import com.dennis.ecommerce.customerservice.messaging.publisher.CustomerUpdatedPublisher;
import com.dennis.ecommerce.customerservice.repository.CustomerRepository;
import com.dennis.ecommerce.customerservice.repository.CustomerStatusHistoryRepository;
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
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerStatusHistoryRepository statusHistoryRepository;
    private final CustomerMapper customerMapper;
    private final CustomerStatusHistoryMapper statusHistoryMapper;
    private final CustomerUpdatedPublisher customerUpdatedPublisher;
    private final CustomerStatusChangedPublisher customerStatusChangedPublisher;

    @Override
    @Transactional
    public CustomerResponse createCustomer(UUID userId, CreateCustomerRequest request) {
        log.info("Creando customer para userId: {}", userId);

        if (customerRepository.existsByUserId(userId)) {
            throw new CustomerAlreadyExistsException("Ya existe un customer para el userId: " + userId);
        }

        Customer customer = customerMapper.toEntity(request);
        customer.setUserId(userId);
        customer.setStatus(CustomerStatus.PENDING_VERIFICATION);

        Customer saved = customerRepository.save(customer);
        log.info("Customer creado con id: {}", saved.getCustomerId());

        return customerMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getByUserId(UUID userId) {
        return customerMapper.toResponse(findByUserIdOrThrow(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getByCustomerId(UUID customerId) {
        return customerMapper.toResponse(findByCustomerIdOrThrow(customerId));
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(UUID userId, UpdateCustomerRequest request) {
        Customer customer = findByUserIdOrThrow(userId);

        customerMapper.updateEntityFromRequest(request, customer);
        Customer updated = customerRepository.save(customer);

        log.info("Customer actualizado: {}", updated.getCustomerId());
        customerUpdatedPublisher.publish(updated);

        return customerMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public CustomerResponse changeStatus(UUID customerId, UUID adminUserId, ChangeCustomerStatusRequest request) {
        Customer customer = findByCustomerIdOrThrow(customerId);

        CustomerStatus previousStatus = customer.getStatus();
        CustomerStatus newStatus = request.getStatus();

        if (previousStatus == newStatus) {
            return customerMapper.toResponse(customer);
        }

        CustomerStatusHistory history = CustomerStatusHistory.builder()
                .customer(customer)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .reason(request.getReason())
                .changedBy(adminUserId)
                .build();

        statusHistoryRepository.save(history);

        customer.setStatus(newStatus);
        Customer updated = customerRepository.save(customer);

        log.info("Customer {} cambió de {} a {}", customerId, previousStatus, newStatus);
        customerStatusChangedPublisher.publish(updated, previousStatus);

        return customerMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerStatusHistoryResponse> getStatusHistory(UUID customerId) {
        findByCustomerIdOrThrow(customerId);
        return statusHistoryRepository
                .findByCustomer_CustomerIdOrderByChangedAtDesc(customerId)
                .stream()
                .map(statusHistoryMapper::toResponse)
                .toList();
    }

    @Override
    public Customer findByUserIdOrThrow(UUID userId) {
        return customerRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer no encontrado para userId: " + userId));
    }

    @Override
    public Customer findByCustomerIdOrThrow(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer no encontrado: " + customerId));
    }
}

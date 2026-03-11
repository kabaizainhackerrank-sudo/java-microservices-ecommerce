package com.dennis.ecommerce.customerservice.repository;

import com.dennis.ecommerce.customerservice.domain.entity.CustomerStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerStatusHistoryRepository extends JpaRepository<CustomerStatusHistory, UUID> {

    List<CustomerStatusHistory> findByCustomer_CustomerIdOrderByChangedAtDesc(UUID customerId);
}

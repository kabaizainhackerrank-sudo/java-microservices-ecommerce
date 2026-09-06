package com.dennis.ecommerce.customerservice.repository;

import com.dennis.ecommerce.auth.domain.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, String> {
    List<OutboxEvent> findByStatusAndRetryCountLessThan(String status, int maxRetries);
}

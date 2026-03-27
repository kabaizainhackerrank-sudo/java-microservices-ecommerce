package com.dennis.ecommerce.customerservice.messaging.event;

import com.dennis.ecommerce.customerservice.domain.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStatusChangedEvent {
    private UUID customerId;
    private UUID userId;
    private CustomerStatus previousStatus;
    private CustomerStatus newStatus;
    private String reason;
}

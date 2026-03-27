package com.dennis.ecommerce.customerservice.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdatedEvent {
    private UUID customerId;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String phone;
}

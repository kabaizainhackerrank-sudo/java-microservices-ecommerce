package com.dennis.ecommerce.customerservice.dto.response;

import com.dennis.ecommerce.customerservice.domain.enums.CustomerStatus;
import com.dennis.ecommerce.customerservice.domain.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private UUID customerId;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String phone;
    private DocumentType documentType;
    private String documentNumber;
    private CustomerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

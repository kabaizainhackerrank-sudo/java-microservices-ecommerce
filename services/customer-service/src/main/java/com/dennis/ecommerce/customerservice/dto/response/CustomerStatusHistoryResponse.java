package com.dennis.ecommerce.customerservice.dto.response;

import com.dennis.ecommerce.customerservice.domain.enums.CustomerStatus;
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
public class CustomerStatusHistoryResponse {

    private UUID historyId;
    private CustomerStatus previousStatus;
    private CustomerStatus newStatus;
    private String reason;
    private UUID changedBy;
    private LocalDateTime changedAt;
}

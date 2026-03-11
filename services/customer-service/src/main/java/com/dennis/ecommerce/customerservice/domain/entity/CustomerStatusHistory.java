package com.dennis.ecommerce.customerservice.domain.entity;

import com.dennis.ecommerce.customerservice.domain.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_status_history", schema = "customer")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "history_id", updatable = false, nullable = false)
    private UUID historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 30)
    private CustomerStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private CustomerStatus newStatus;

    @Column(name = "reason", length = 500)
    private String reason;

    // userId de quien hizo el cambio (admin o sistema)
    @Column(name = "changed_by")
    private UUID changedBy;

    @CreationTimestamp
    @Column(name = "changed_at", updatable = false)
    private LocalDateTime changedAt;
}

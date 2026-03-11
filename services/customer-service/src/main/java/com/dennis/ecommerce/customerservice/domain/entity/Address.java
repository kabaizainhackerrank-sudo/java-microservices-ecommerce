package com.dennis.ecommerce.customerservice.domain.entity;

import com.dennis.ecommerce.customerservice.domain.enums.AddressLabel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/// En las entidades JPA es peligroso usar @Data porque:
///
/// @EqualsAndHashCode puede causar loops infinitos si tienes relaciones bidireccionales (Customer → Address → Customer → ...)
/// @ToString tiene el mismo problema con relaciones lazy
///

@Entity
@Table(name = "addresses", schema = "customer")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id", updatable = false, nullable = false)
    private UUID addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false, length = 30)
    private AddressLabel label;

    @Column(name = "street", nullable = false, length = 200)
    private String street;

    @Column(name = "number", length = 20)
    private String number;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

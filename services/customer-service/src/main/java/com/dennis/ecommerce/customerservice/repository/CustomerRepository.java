package com.dennis.ecommerce.customerservice.repository;

import com.dennis.ecommerce.customerservice.domain.entity.Customer;
import com.dennis.ecommerce.customerservice.domain.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    boolean existsByDocumentTypeAndDocumentNumber(DocumentType documentType,
                                                  String documentNumber
    );
}
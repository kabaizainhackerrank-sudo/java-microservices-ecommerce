package com.dennis.ecommerce.customerservice.repository;

import com.dennis.ecommerce.customerservice.domain.entity.CustomerPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerPreferencesRepository extends JpaRepository<CustomerPreferences, UUID> {

    //findByCustomer_CustomerId: la notación con _ le dice a Spring Data que navegue la relación.
    // Es equivalente a JOIN customer WHERE customer_id = ? pero sin escribir JPQL.
    Optional<CustomerPreferences> findByCustomer_CustomerId(UUID customerId);
}

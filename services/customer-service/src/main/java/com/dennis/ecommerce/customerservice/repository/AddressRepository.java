package com.dennis.ecommerce.customerservice.repository;

import com.dennis.ecommerce.customerservice.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    //findByCustomer_CustomerId: la notación con _ le dice a Spring Data que navegue la relación.
    // Es equivalente a JOIN customer WHERE customer_id = ? pero sin escribir JPQL.
    List<Address> findByCustomer_CustomerId(UUID customerId);

    Optional<Address> findByAddressIdAndCustomer_CustomerId(UUID addressId, UUID customerId);

    boolean existsByAddressIdAndCustomer_CustomerId(UUID addressId, UUID customerId);

    // Desmarcar todas las direcciones default de un cliente
    // antes de marcar una nueva como default
    //necesita @Modifying porque es una operación de escritura con JPQL. Se llama dentro de una transacción justo antes
    // de marcar la nueva dirección como default.
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.customer.customerId = :customerId")
    void clearDefaultByCustomerId(@Param("customerId") UUID customerId);
}
package com.dennis.ecommerce.catalogService.repository

import com.dennis.ecommerce.catalogService.domain.entity.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StockRepository : JpaRepository<Stock, Long> {

    // Buscar stock por producto
    fun findByProductId(productId: Long): Stock?

    // Buscar productos con stock disponible
    fun findByQuantityGreaterThan(quantity: Int): List<Stock>


    // creamos dos querys de JPQL
    // Decrementar stock directamente en BD (para order.confirmed)
    @Modifying
    @Query("UPDATE Stock s SET s.quantity = s.quantity - :qty WHERE s.product.id = :productId")
    fun decrementStock(@Param("productId") productId: Long, @Param("qty") qty: Int)

    // Incrementar stock directamente en BD (para order.cancelled)
    @Modifying
    @Query("UPDATE Stock s SET s.quantity = s.quantity + :qty WHERE s.product.id = :productId")
    fun incrementStock(@Param("productId") productId: Long, @Param("qty") qty: Int)
}
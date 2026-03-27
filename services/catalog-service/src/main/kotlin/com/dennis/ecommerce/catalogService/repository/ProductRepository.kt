package com.dennis.ecommerce.catalogService.repository

import com.dennis.ecommerce.catalogService.domain.entity.Product
import com.dennis.ecommerce.catalogService.domain.enums.ProductStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
interface ProductRepository : JpaRepository<Product, Long> {

    // Buscar por nombre (contiene el texto)
    fun findByNameContainingIgnoreCase(name: String): List<Product>

    // Buscar por categoría
    fun findByCategoryId(categoryId: Long): List<Product>

    // Buscar por status
    fun findByStatus(status: ProductStatus): List<Product>

    // Buscar por SKU
    fun findBySku(sku: String): Product?

    // Verificar si existe el SKU
    fun existsBySku(sku: String): Boolean

    // Buscar por rango de precio
    fun findByPriceBetween(min: BigDecimal, max: BigDecimal): List<Product>

    // Buscar por nombre Y categoría Y precio
    @Query("""
        SELECT p FROM Product p 
        WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:categoryId IS NULL OR p.category.id = :categoryId)
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        AND (:status IS NULL OR p.status = :status)
    """)

    // El usuario desde el frontend manda solo lo que quiere filtrar
    fun search(
        @Param("name") name: String?,
        @Param("categoryId") categoryId: Long?,
        @Param("minPrice") minPrice: BigDecimal?,
        @Param("maxPrice") maxPrice: BigDecimal?,
        @Param("status") status: ProductStatus?
    ): List<Product>
}
package com.dennis.ecommerce.catalogService.service.interfaces

import com.dennis.ecommerce.catalogService.domain.entity.Product
import com.dennis.ecommerce.catalogService.domain.enums.ProductStatus
import java.math.BigDecimal

interface ProductService {
    fun findAll(): List<Product>
    fun findById(id: Long): Product
    fun findBySku(sku: String): Product
    fun search(name: String?, categoryId: Long?, minPrice: BigDecimal?, maxPrice: BigDecimal?): List<Product>
    fun create(name: String, description: String?, price: BigDecimal, sku: String, categoryId: Long, initialStock: Int): Product
    fun update(id: Long, name: String, description: String?, price: BigDecimal, categoryId: Long): Product
    fun changeStatus(id: Long, status: ProductStatus): Product
    fun delete(id: Long)
}
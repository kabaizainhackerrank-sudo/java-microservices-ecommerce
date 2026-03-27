package com.dennis.ecommerce.catalogService.dto.response

import com.dennis.ecommerce.catalogService.domain.enums.ProductStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val sku: String,
    val status: ProductStatus,
    val category: CategoryResponse,
    val stock: StockResponse?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
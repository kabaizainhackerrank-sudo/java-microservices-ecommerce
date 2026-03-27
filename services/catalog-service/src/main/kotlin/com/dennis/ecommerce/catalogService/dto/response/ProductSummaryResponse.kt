package com.dennis.ecommerce.catalogService.dto.response

import com.dennis.ecommerce.catalogService.domain.enums.ProductStatus
import java.math.BigDecimal

// Para listar productos sin todos los detalles
data class ProductSummaryResponse(
    val id: Long,
    val name: String,
    val price: BigDecimal,
    val sku: String,
    val status: ProductStatus,
    val categoryName: String,
    val stockAvailable: Int
)
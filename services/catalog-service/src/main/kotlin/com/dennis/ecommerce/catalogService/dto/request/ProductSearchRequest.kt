package com.dennis.ecommerce.catalogService.dto.request

import java.math.BigDecimal

data class ProductSearchRequest(
    val name: String? = null,
    val categoryId: Long? = null,
    val minPrice: BigDecimal? = null,
    val maxPrice: BigDecimal? = null
)

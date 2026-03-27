package com.dennis.ecommerce.catalogService.dto.request

import com.dennis.ecommerce.catalogService.domain.enums.ProductStatus
import jakarta.validation.constraints.NotNull

data class ProductStatusRequest(
    @field:NotNull(message = "El estado es obligatorio")
    val status: ProductStatus
)

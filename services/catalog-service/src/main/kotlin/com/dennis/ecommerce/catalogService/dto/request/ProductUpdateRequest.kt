package com.dennis.ecommerce.catalogService.dto.request

import jakarta.validation.constraints.*
import java.math.BigDecimal

data class ProductUpdateRequest(
    @field:NotBlank(message = "El nombre es obligatorio")
    @field:Size(min = 2, max = 255)
    val name: String,

    val description: String? = null,

    @field:NotNull(message = "El precio es obligatorio")
    @field:DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    val price: BigDecimal,

    @field:NotNull(message = "La categoría es obligatoria")
    val categoryId: Long
)

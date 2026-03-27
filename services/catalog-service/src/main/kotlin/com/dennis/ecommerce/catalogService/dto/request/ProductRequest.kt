package com.dennis.ecommerce.catalogService.dto.request

import jakarta.validation.constraints.*
import java.math.BigDecimal

data class ProductRequest(
    @field:NotBlank(message = "El nombre es obligatorio")
    @field:Size(min = 2, max = 255, message = "El nombre debe tener entre 2 y 255 caracteres")
    val name: String,

    val description: String? = null,

    @field:NotNull(message = "El precio es obligatorio")
    @field:DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    val price: BigDecimal,

    @field:NotBlank(message = "El SKU es obligatorio")
    @field:Size(max = 100, message = "El SKU no puede tener más de 100 caracteres")
    val sku: String,

    @field:NotNull(message = "La categoría es obligatoria")
    val categoryId: Long,

    @field:Min(value = 0, message = "El stock inicial no puede ser negativo")
    val initialStock: Int = 0
)
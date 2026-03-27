package com.dennis.ecommerce.catalogService.dto.response

import java.time.LocalDateTime

data class StockResponse(
    val id: Long,
    val quantity: Int,
    val isInStock: Boolean,
    val updatedAt: LocalDateTime
)

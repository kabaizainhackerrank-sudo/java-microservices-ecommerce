package com.dennis.ecommerce.catalogService.messaging.event

import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductCreatedEvent(
    val productId: Long,
    val name: String,
    val categoryId: Long,
    val price: BigDecimal,
    val stockAvailable: Int,
    val createdAt: LocalDateTime = LocalDateTime.now()
)


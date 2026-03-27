package com.dennis.ecommerce.catalogService.messaging.event

import java.time.LocalDateTime

data class StockUpdatedEvent(
    val productId: Long,
    val previousStock: Int,
    val newStock: Int,
    val reason: String,  // ORDER_CONFIRMED, ORDER_CANCELLED
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

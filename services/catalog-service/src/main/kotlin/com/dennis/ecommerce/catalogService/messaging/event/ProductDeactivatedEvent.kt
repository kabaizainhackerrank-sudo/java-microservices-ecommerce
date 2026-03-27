package com.dennis.ecommerce.catalogService.messaging.event

import java.time.LocalDateTime

data class ProductDeactivatedEvent(
    val productId: Long,
    val name: String,
    val reason: String? = null,
    val deactivatedAt: LocalDateTime = LocalDateTime.now()
)

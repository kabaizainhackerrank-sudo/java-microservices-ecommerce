package com.dennis.ecommerce.catalogService.messaging.event

// evento que consume del order-service
data class OrderConfirmedEvent(
    val orderId: Long,
    val items: List<OrderItemEvent>
)

data class OrderItemEvent(
    val productId: Long,
    val quantity: Int
)

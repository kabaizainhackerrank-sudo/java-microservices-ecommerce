package com.dennis.ecommerce.catalogService.messaging.event

data class OrderCancelledEvent(
    val orderId: Long,
    val items: List<OrderItemEvent>
)

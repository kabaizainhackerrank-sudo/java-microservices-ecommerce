package com.dennis.ecommerce.catalogService.messaging.consumer

import com.dennis.ecommerce.catalogService.messaging.event.OrderConfirmedEvent
import com.dennis.ecommerce.catalogService.messaging.event.StockUpdatedEvent
import com.dennis.ecommerce.catalogService.messaging.publisher.ProductEventPublisher
import com.dennis.ecommerce.catalogService.service.interfaces.StockService
import com.rabbitmq.client.Channel
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component
class OrderConfirmedConsumer(
    private val stockService: StockService,
    private val productEventPublisher: ProductEventPublisher
) {
    private val log = LoggerFactory.getLogger(OrderConfirmedConsumer::class.java)

    @RabbitListener(queues = ["\${messaging.queues.order-confirmed}"])
    fun consume(
        event: OrderConfirmedEvent,
        channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long
    ) {
        try {
            log.info("Procesando order.confirmed orderId=${event.orderId}")

            event.items.forEach { item ->
                val previousStock = stockService.getStock(item.productId)
                stockService.decrementStock(item.productId, item.quantity)
                val newStock = stockService.getStock(item.productId)

                productEventPublisher.publishStockUpdated(
                    StockUpdatedEvent(
                        productId = item.productId,
                        previousStock = previousStock,
                        newStock = newStock,
                        reason = "ORDER_CONFIRMED"
                    )
                )
            }

            channel.basicAck(tag, false)  // ACK manual — confirma procesamiento exitoso

        } catch (ex: Exception) {
            log.error("Error procesando order.confirmed orderId=${event.orderId}: ${ex.message}")
            channel.basicNack(tag, false, false)  // NACK — va al dead letter
        }
    }
}
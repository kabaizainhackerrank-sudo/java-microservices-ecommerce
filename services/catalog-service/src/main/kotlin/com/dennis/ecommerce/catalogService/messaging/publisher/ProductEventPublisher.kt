package com.dennis.ecommerce.catalogService.messaging.publisher


import com.dennis.ecommerce.catalogService.messaging.event.ProductCreatedEvent
import com.dennis.ecommerce.catalogService.messaging.event.ProductDeactivatedEvent
import com.dennis.ecommerce.catalogService.messaging.event.StockUpdatedEvent
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ProductEventPublisher(
    private val rabbitTemplate: RabbitTemplate,

    @Value("\${messaging.exchange.catalog}")
    private val catalogExchange: String,

    @Value("\${messaging.routing-keys.product-created}")
    private val productCreatedKey: String,

    @Value("\${messaging.routing-keys.product-deactivated}")
    private val productDeactivatedKey: String,

    @Value("\${messaging.routing-keys.stock-updated}")
    private val stockUpdatedKey: String
) {
    private val log = LoggerFactory.getLogger(ProductEventPublisher::class.java)

    fun publishProductCreated(event: ProductCreatedEvent) {
        rabbitTemplate.convertAndSend(catalogExchange, productCreatedKey, event)
        log.info("Evento product.created publicado: productId=${event.productId}")
    }

    fun publishProductDeactivated(event: ProductDeactivatedEvent) {
        rabbitTemplate.convertAndSend(catalogExchange, productDeactivatedKey, event)
        log.info("Evento product.deactivated publicado: productId=${event.productId}")
    }

    fun publishStockUpdated(event: StockUpdatedEvent) {
        rabbitTemplate.convertAndSend(catalogExchange, stockUpdatedKey, event)
        log.info("Evento stock.updated publicado: productId=${event.productId} stock=${event.previousStock}→${event.newStock}")
    }
}
package com.dennis.ecommerce.catalogService.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    // ── Exchanges ─────────────────────────────────────────
    @Value("\${messaging.exchange.catalog}")
    private lateinit var catalogExchange: String

    @Value("\${messaging.exchange.order}")
    private lateinit var orderExchange: String

    // ── Queues ────────────────────────────────────────────
    @Value("\${messaging.queues.order-confirmed}")
    private lateinit var orderConfirmedQueue: String

    @Value("\${messaging.queues.order-cancelled}")
    private lateinit var orderCancelledQueue: String

    // ── Routing Keys ──────────────────────────────────────
    @Value("\${messaging.routing-keys.order-confirmed}")
    private lateinit var orderConfirmedRoutingKey: String

    @Value("\${messaging.routing-keys.order-cancelled}")
    private lateinit var orderCancelledRoutingKey: String

    // ── Exchanges ─────────────────────────────────────────

    // Exchange propio del catalog-service
    @Bean
    fun catalogExchange(): TopicExchange =
        TopicExchange(catalogExchange, true, false)

    // Exchange del order-service (solo lo referenciamos, no lo creamos porque ya debe haber sido cerado por el order-service cuando p[ubliquen los mensajes que leeremos aqui)
    @Bean
    fun orderExchange(): TopicExchange =
        TopicExchange(orderExchange, true, false)

    // ── Queues ────────────────────────────────────────────

    @Bean
    fun orderConfirmedQueue(): Queue =
        QueueBuilder.durable(orderConfirmedQueue)
            .withArgument("x-dead-letter-exchange", "$catalogExchange.dlx")
            .build()

    @Bean
    fun orderCancelledQueue(): Queue =
        QueueBuilder.durable(orderCancelledQueue)
            .withArgument("x-dead-letter-exchange", "$catalogExchange.dlx")
            .build()

    // ── Bindings ──────────────────────────────────────────

    // Escucha eventos del order-service
    @Bean
    fun orderConfirmedBinding(): Binding =
        BindingBuilder
            .bind(orderConfirmedQueue())
            .to(orderExchange())
            .with(orderConfirmedRoutingKey)

    @Bean
    fun orderCancelledBinding(): Binding =
        BindingBuilder
            .bind(orderCancelledQueue())
            .to(orderExchange())
            .with(orderCancelledRoutingKey)

    // ── Serialización JSON ────────────────────────────────

    @Bean
    fun jsonMessageConverter(): MessageConverter =
        Jackson2JsonMessageConverter()

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate =
        RabbitTemplate(connectionFactory).apply {
            messageConverter = jsonMessageConverter()
        }
}
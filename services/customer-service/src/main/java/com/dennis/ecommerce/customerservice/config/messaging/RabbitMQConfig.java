package com.dennis.ecommerce.customerservice.config.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    //Los eventos son simples DTOs** — no contienen lógica, solo datos. Cualquier servicio que los consuma no necesita
    // conocer las clases internas del `customer-service`.

    // ── Exchanges ──────────────────────────────────────────
    @Value("${messaging.exchange.auth}")
    private String authExchange;

    @Value("${messaging.exchange.customer}")
    private String customerExchange;

    // ── Queues ─────────────────────────────────────────────
    @Value("${messaging.queues.user-registered}")
    private String userRegisteredQueue;

    @Value("${messaging.queues.customer-updated}")
    private String customerUpdatedQueue;

    @Value("${messaging.queues.customer-status-changed}")
    private String customerStatusChangedQueue;

    // ── Routing Keys ───────────────────────────────────────
    @Value("${messaging.routing-keys.user-registered}")
    private String userRegisteredRoutingKey;

    @Value("${messaging.routing-keys.customer-updated}")
    private String customerUpdatedRoutingKey;

    @Value("${messaging.routing-keys.customer-status-changed}")
    private String customerStatusChangedRoutingKey;

    // ── Declaración de Exchanges ───────────────────────────

    //`TopicExchange`** — permite routing keys con patrones como `user.*` o `customer.#`. Es el tipo más flexible y el estándar para microservicios.
    // Exchange del auth-service (ya existe, solo lo referenciamos)
    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange(authExchange, true, false);
    }

    // Exchange propio del customer-service
    @Bean
    public TopicExchange customerExchange() {
        return new TopicExchange(customerExchange, true, false);
    }

    // ── Declaración de Queues ──────────────────────────────

    @Bean
    public Queue userRegisteredQueue() {
        return QueueBuilder.durable().build();
    }

    @Bean
    public Queue customerUpdatedQueue() {
        return QueueBuilder.durable(customerUpdatedQueue)
                .withArgument("x-dead-letter-exchange", customerExchange + ".dlx")
                .build();
    }

    @Bean
    public Queue customerStatusChangedQueue() {
        return QueueBuilder.durable(customerStatusChangedQueue)
                .withArgument("x-dead-letter-exchange", customerExchange + ".dlx")
                .build();
    }

    // ── Bindings ───────────────────────────────────────────

    // Escucha eventos del auth-service
    @Bean
    public Binding userRegisteredBinding() {
        return BindingBuilder
                .bind(userRegisteredQueue())
                .to(authExchange())
                .with(userRegisteredRoutingKey);
    }

    // Publica eventos propios
    @Bean
    public Binding customerUpdatedBinding() {
        return BindingBuilder
                .bind(customerUpdatedQueue())
                .to(customerExchange())
                .with(customerUpdatedRoutingKey);
    }

    @Bean
    public Binding customerStatusChangedBinding() {
        return BindingBuilder
                .bind(customerStatusChangedQueue())
                .to(customerExchange())
                .with(customerStatusChangedRoutingKey);
    }

    // ── Serialización JSON ─────────────────────────────────

    @Bean
    public MessageConverter jsonMessageConverter() {
        //`Jackson2JsonMessageConverter`** — serializa los eventos como JSON automáticamente. Sin esto RabbitMQ
        // enviaría bytes serializados de Java que ningún otro servicio podría leer.
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}

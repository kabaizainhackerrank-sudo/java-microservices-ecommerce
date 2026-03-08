package com.dennis.ecommerce.auth.event;

import com.dennis.ecommerce.auth.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishUserCreated(UserCreatedEvent event) {
        log.info("Publicando evento user.created para: {}", event.getEmail());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EXCHANGE,
                RabbitMQConfig.USER_CREATED_ROUTING_KEY,
                event
        );
    }
}

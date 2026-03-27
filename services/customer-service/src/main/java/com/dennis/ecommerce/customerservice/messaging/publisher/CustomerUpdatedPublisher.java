package com.dennis.ecommerce.customerservice.messaging.publisher;

import com.dennis.ecommerce.customerservice.domain.entity.Customer;
import com.dennis.ecommerce.customerservice.messaging.event.CustomerUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerUpdatedPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${messaging.exchange.customer}")
    private String customerExchange;

    @Value("${messaging.routing-keys.customer-updated}")
    private String routingKey;

    public void publish(Customer customer) {
        CustomerUpdatedEvent event = CustomerUpdatedEvent.builder()
                .customerId(customer.getCustomerId())
                .userId(customer.getUserId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phone(customer.getPhone())
                .build();

        rabbitTemplate.convertAndSend(customerExchange, routingKey, event);
        log.info("Evento customer.updated publicado para customerId: {}", customer.getCustomerId());
    }
}
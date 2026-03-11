package com.dennis.ecommerce.customerservice.messaging.publisher;

import com.dennis.ecommerce.customerservice.domain.entity.Customer;
import com.dennis.ecommerce.customerservice.domain.enums.CustomerStatus;
import com.dennis.ecommerce.customerservice.messaging.event.CustomerStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerStatusChangedPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${messaging.exchange.customer}")// busca en el fichero application.yml el valor de esta variable y
    private String customerExchange;// se lo inyecta a este campo

    @Value("${messaging.routing-keys.customer-status-changed}")
    private String routingKey;

    public void publish(Customer customer, CustomerStatus previousStatus) {
        CustomerStatusChangedEvent event = CustomerStatusChangedEvent.builder()
                .customerId(customer.getCustomerId())
                .userId(customer.getUserId())
                .previousStatus(previousStatus)
                .newStatus(customer.getStatus())
                .build();

        rabbitTemplate.convertAndSend(customerExchange, routingKey, event);
        log.info("Evento customer.status.changed publicado: {} → {}",
                previousStatus, customer.getStatus());
    }
}
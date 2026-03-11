package com.dennis.ecommerce.customerservice.messaging.consumer;

import com.dennis.ecommerce.customerservice.dto.request.CreateCustomerRequest;
import com.dennis.ecommerce.customerservice.messaging.event.UserRegisteredEvent;
import com.dennis.ecommerce.customerservice.service.interfaces.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegisteredConsumer {

    private final CustomerService customerService;

    @RabbitListener(queues = "${messaging.queues.user-registered}")
    public void consume(UserRegisteredEvent event,
                        Channel channel,
                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        log.info("Evento recibido user.registered para userId: {}", event.getUserId());

        try {
            CreateCustomerRequest request = CreateCustomerRequest.builder().build();
            customerService.createCustomer(event.getUserId(), request);

            // ACK — mensaje procesado correctamente
            //**ACK manual** — con `acknowledge-mode: manual` del `application.yml`, tú controlas cuándo confirmar el mensaje.
            // Si el `createCustomer` falla, el mensaje no se pierde.
            channel.basicAck(deliveryTag, false);
            log.info("Customer creado exitosamente para userId: {}", event.getUserId());

        } catch (Exception e) {
            log.error("Error procesando user.registered para userId: {}", event.getUserId(), e);
            // NACK — no reencolar, va al dead letter
            //Dead Letter Queue** — cuando un mensaje falla y se hace `basicNack`, en lugar de perderse va a una cola
            //especial para revisión. Lo configuramos con `x-dead-letter-exchange`.
            channel.basicNack(deliveryTag, false, false);
        }
    }
}

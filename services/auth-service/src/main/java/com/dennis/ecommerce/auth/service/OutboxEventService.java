package com.dennis.ecommerce.auth.service;

import com.dennis.ecommerce.auth.domain.entity.OutboxEvent;
import com.dennis.ecommerce.auth.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dennis.ecommerce.auth.config.RabbitMQConfig;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    // Guarda el evento en la tabla outbox — dentro de la transacción del negocio
    public void saveEvent(String eventType, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            OutboxEvent event = OutboxEvent.builder()
                    .eventType(eventType)
                    .payload(json)
                    .retryCount(0)
                    .build();
            outboxEventRepository.save(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializando evento outbox", e);
        }
    }

    // Worker que corre cada 5 segundos y publica los eventos pendientes
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxEventRepository
                .findByStatusAndRetryCountLessThan("PENDING", 3);

        for (OutboxEvent event : pendingEvents) {
            try {
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.USER_EXCHANGE,
                        event.getEventType(),
                        event.getPayload()
                );
                event.setStatus("PUBLISHED");
                event.setPublishedAt(LocalDateTime.now());
                log.info("Evento publicado: {}", event.getEventType());
            } catch (Exception e) {
                event.setRetryCount(event.getRetryCount() + 1);
                if (event.getRetryCount() >= 3) {
                    event.setStatus("FAILED");
                    log.error("Evento fallido después de 3 intentos: {}", event.getEventType());
                }
            }
            outboxEventRepository.save(event);
        }
    }
}
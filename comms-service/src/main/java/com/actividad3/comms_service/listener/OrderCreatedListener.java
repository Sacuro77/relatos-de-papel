package com.actividad3.comms_service.listener;

import com.actividad3.comms_service.event.OrderCreatedEvent;
import com.actividad3.comms_service.service.EmailNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final ObjectMapper objectMapper;
    private final EmailNotificationService emailNotificationService;

    public OrderCreatedListener(ObjectMapper objectMapper, EmailNotificationService emailNotificationService) {
        this.objectMapper = objectMapper;
        this.emailNotificationService = emailNotificationService;
    }

    @RabbitListener(queues = "${app.rabbitmq.orders.created-queue}")
    public void handleOrderCreated(Message message) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(message.getBody(), OrderCreatedEvent.class);
            log.info(
                    "OrderCreated recibido orderId={} userId={} email={} total={}",
                    event.getOrderId(),
                    event.getUserId(),
                    event.getUserEmail(),
                    event.getTotal()
            );
            emailNotificationService.notifyOrderCreated(event);
        } catch (Exception ex) {
            log.error("No se pudo procesar mensaje OrderCreated. payload={}", new String(message.getBody()), ex);
            throw new IllegalArgumentException("Mensaje OrderCreated invalido", ex);
        }
    }
}

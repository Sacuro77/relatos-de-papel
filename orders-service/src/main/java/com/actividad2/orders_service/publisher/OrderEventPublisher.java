package com.actividad2.orders_service.publisher;

import com.actividad2.orders_service.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String orderCreatedRoutingKey;

    public OrderEventPublisher(
            RabbitTemplate rabbitTemplate,
            JacksonJsonMessageConverter jacksonJsonMessageConverter,
            @Value("${app.rabbitmq.orders.exchange}") String exchange,
            @Value("${app.rabbitmq.orders.created-routing-key}") String orderCreatedRoutingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(jacksonJsonMessageConverter);
        this.exchange = exchange;
        this.orderCreatedRoutingKey = orderCreatedRoutingKey;
    }

    public void publishOrderCreated(OrderCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(exchange, orderCreatedRoutingKey, event);
            log.info(
                    "OrderCreated publicado orderId={} userId={} routingKey={}",
                    event.getOrderId(),
                    event.getUserId(),
                    orderCreatedRoutingKey
            );
        } catch (AmqpException ex) {
            log.error(
                    "No se pudo publicar OrderCreated orderId={} userId={} routingKey={}. El pedido ya fue persistido y queda pendiente de consistencia eventual.",
                    event.getOrderId(),
                    event.getUserId(),
                    orderCreatedRoutingKey,
                    ex
            );
        }
    }
}

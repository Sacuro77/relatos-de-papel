package com.actividad3.comms_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public DirectExchange ordersExchange(@Value("${app.rabbitmq.orders.exchange}") String exchangeName) {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue orderCreatedQueue(@Value("${app.rabbitmq.orders.created-queue}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding orderCreatedBinding(
            Queue orderCreatedQueue,
            DirectExchange ordersExchange,
            @Value("${app.rabbitmq.orders.created-routing-key}") String routingKey
    ) {
        return BindingBuilder.bind(orderCreatedQueue)
                .to(ordersExchange)
                .with(routingKey);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}

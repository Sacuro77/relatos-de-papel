package com.actividad3.comms_service.service;

import com.actividad3.comms_service.config.MailProperties;
import com.actividad3.comms_service.event.OrderCreatedEvent;
import com.actividad3.comms_service.event.OrderCreatedItemEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class EmailNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

    private final MailProperties mailProperties;
    private final SmtpEmailClient smtpEmailClient;

    public EmailNotificationService(MailProperties mailProperties, SmtpEmailClient smtpEmailClient) {
        this.mailProperties = mailProperties;
        this.smtpEmailClient = smtpEmailClient;
    }

    public void notifyOrderCreated(OrderCreatedEvent event) {
        String subject = "Pedido confirmado #" + event.getOrderId();
        String body = buildBody(event);

        if (!mailProperties.isEnabled()) {
            log.info(
                    "MAIL MOCK to={} from={} subject=\"{}\" body=\"{}\"",
                    event.getUserEmail(),
                    mailProperties.getFrom(),
                    subject,
                    body.replace(System.lineSeparator(), " | ")
            );
            return;
        }

        try {
            smtpEmailClient.send(event.getUserEmail(), subject, body);
            log.info("Email de confirmacion enviado orderId={} to={}", event.getOrderId(), event.getUserEmail());
        } catch (RuntimeException ex) {
            log.error("No se pudo enviar email SMTP orderId={} to={}. Se registra contenido simulado.", event.getOrderId(), event.getUserEmail(), ex);
            log.info("MAIL MOCK to={} from={} subject=\"{}\" body=\"{}\"", event.getUserEmail(), mailProperties.getFrom(), subject, body.replace(System.lineSeparator(), " | "));
        }
    }

    private String buildBody(OrderCreatedEvent event) {
        return """
                Hola,

                Tu pedido #%s fue registrado correctamente.

                Usuario: %s
                Estado: %s
                Total: %s

                Items:
                %s

                Gracias por comprar en Relatos de Papel.
                """.formatted(
                event.getOrderId(),
                event.getUserId(),
                event.getStatus(),
                event.getTotal(),
                formatItems(event)
        );
    }

    private String formatItems(OrderCreatedEvent event) {
        if (event.getItems() == null || event.getItems().isEmpty()) {
            return "- Sin items";
        }

        return event.getItems()
                .stream()
                .map(this::formatItem)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String formatItem(OrderCreatedItemEvent item) {
        return "- %s x%d - unitario: %s - subtotal: %s".formatted(
                item.getBookTitle(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
}

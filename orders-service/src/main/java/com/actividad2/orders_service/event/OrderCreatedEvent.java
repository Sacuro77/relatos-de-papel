package com.actividad2.orders_service.event;

import com.actividad2.orders_service.entity.PurchaseOrder;
import com.actividad2.orders_service.security.AuthenticatedUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderCreatedEvent {

    private String eventId;
    private String eventType;
    private Long orderId;
    private String userId;
    private String userEmail;
    private String status;
    private BigDecimal total;
    private List<OrderCreatedItemEvent> items;
    private LocalDateTime createdAt;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(
            String eventId,
            String eventType,
            Long orderId,
            String userId,
            String userEmail,
            String status,
            BigDecimal total,
            List<OrderCreatedItemEvent> items,
            LocalDateTime createdAt
    ) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.orderId = orderId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.status = status;
        this.total = total;
        this.items = items;
        this.createdAt = createdAt;
    }

    public static OrderCreatedEvent from(PurchaseOrder order, AuthenticatedUser authenticatedUser) {
        List<OrderCreatedItemEvent> itemEvents = order.getItems()
                .stream()
                .map(OrderCreatedItemEvent::from)
                .toList();

        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                "OrderCreated",
                order.getId(),
                order.getUserId(),
                authenticatedUser.getEmail(),
                order.getStatus(),
                order.getTotal(),
                itemEvents,
                order.getCreatedAt()
        );
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<OrderCreatedItemEvent> getItems() {
        return items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

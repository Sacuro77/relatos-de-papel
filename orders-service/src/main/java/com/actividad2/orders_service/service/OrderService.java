package com.actividad2.orders_service.service;

import com.actividad2.orders_service.client.CatalogueClient;
import com.actividad2.orders_service.dto.BookAvailabilityResponse;
import com.actividad2.orders_service.dto.OrderItemRequest;
import com.actividad2.orders_service.dto.OrderRequest;
import com.actividad2.orders_service.dto.OrderResponse;
import com.actividad2.orders_service.entity.OrderItem;
import com.actividad2.orders_service.entity.PurchaseOrder;
import com.actividad2.orders_service.repository.PurchaseOrderRepository;
import com.actividad2.orders_service.security.AuthenticatedUser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CatalogueClient catalogueClient;

    public OrderService(
            PurchaseOrderRepository purchaseOrderRepository,
            CatalogueClient catalogueClient
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.catalogueClient = catalogueClient;
    }

    public OrderResponse create(OrderRequest request, AuthenticatedUser authenticatedUser) {
        PurchaseOrder order = PurchaseOrder.builder()
                .userId(authenticatedUser.getUserId())
                .status("CREATED")
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal orderTotal = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            BookAvailabilityResponse availability = catalogueClient.checkAvailability(
                    itemRequest.getBookId(),
                    itemRequest.getQuantity()
            );

            if (availability == null) {
                throw new RuntimeException("No se pudo validar el libro con catalogue-service");
            }

            if (!Boolean.TRUE.equals(availability.getExists())) {
                throw new RuntimeException("El libro no existe. bookId: " + itemRequest.getBookId());
            }

            if (!Boolean.TRUE.equals(availability.getVisible())) {
                throw new RuntimeException("El libro no está visible. bookId: " + itemRequest.getBookId());
            }

            if (!Boolean.TRUE.equals(availability.getAvailable())) {
                throw new RuntimeException("El libro no está disponible. Motivo: " + availability.getMessage());
            }

            BigDecimal unitPrice = availability.getPrice() == null
                    ? BigDecimal.ZERO
                    : availability.getPrice();

            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderItem item = OrderItem.builder()
                    .bookId(itemRequest.getBookId())
                    .bookTitle(availability.getTitle())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            order.addItem(item);
            orderTotal = orderTotal.add(subtotal);
        }

        order.setTotal(orderTotal);

        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getStatus(),
                "Compra registrada correctamente"
        );
    }

    public List<PurchaseOrder> findRecentByUserId(String userId) {
        return purchaseOrderRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
    }

    public PurchaseOrder findById(Long orderId) {
        return purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con id: " + orderId));
    }

    public PurchaseOrder findByIdForUser(Long orderId, String userId) {
        PurchaseOrder order = findById(orderId);
        if (!userId.equals(order.getUserId())) {
            throw new RuntimeException("Orden no encontrada con id: " + orderId);
        }
        return order;
    }
}

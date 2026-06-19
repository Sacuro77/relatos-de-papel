package com.actividad2.orders_service.controller;

import com.actividad2.orders_service.dto.OrderRequest;
import com.actividad2.orders_service.dto.OrderResponse;
import com.actividad2.orders_service.entity.PurchaseOrder;
import com.actividad2.orders_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse create(@Valid @RequestBody OrderRequest request) {
        return orderService.create(request);
    }

    @GetMapping("/recent/{userId}")
    public List<PurchaseOrder> findRecentByUserId(@PathVariable String userId) {
        return orderService.findRecentByUserId(userId);
    }

    @GetMapping("/{orderId}")
    public PurchaseOrder findById(@PathVariable Long orderId) {
        return orderService.findById(orderId);
    }
}
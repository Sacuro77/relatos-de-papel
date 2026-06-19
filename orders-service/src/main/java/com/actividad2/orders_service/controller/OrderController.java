package com.actividad2.orders_service.controller;

import com.actividad2.orders_service.dto.OrderRequest;
import com.actividad2.orders_service.dto.OrderResponse;
import com.actividad2.orders_service.entity.PurchaseOrder;
import com.actividad2.orders_service.security.AuthenticatedUser;
import com.actividad2.orders_service.security.InternalJwtService;
import com.actividad2.orders_service.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final InternalJwtService internalJwtService;
    private final String accessTokenHeader;

    public OrderController(
            OrderService orderService,
            InternalJwtService internalJwtService,
            @Value("${app.security.access-token-header}") String accessTokenHeader
    ) {
        this.orderService = orderService;
        this.internalJwtService = internalJwtService;
        this.accessTokenHeader = accessTokenHeader;
    }

    @PostMapping
    public OrderResponse create(HttpServletRequest servletRequest,
                                @Valid @RequestBody OrderRequest request) {
        AuthenticatedUser authenticatedUser = authenticate(servletRequest);
        return orderService.create(request, authenticatedUser);
    }

    @GetMapping("/recent")
    public List<PurchaseOrder> findRecent(HttpServletRequest servletRequest) {
        AuthenticatedUser authenticatedUser = authenticate(servletRequest);
        return orderService.findRecentByUserId(authenticatedUser.getUserId());
    }

    @GetMapping("/recent/{userId}")
    public List<PurchaseOrder> findRecentByUserId(HttpServletRequest servletRequest,
                                                  @PathVariable String userId) {
        AuthenticatedUser authenticatedUser = authenticate(servletRequest);
        return orderService.findRecentByUserId(authenticatedUser.getUserId());
    }

    @GetMapping("/{orderId}")
    public PurchaseOrder findById(HttpServletRequest servletRequest,
                                  @PathVariable Long orderId) {
        AuthenticatedUser authenticatedUser = authenticate(servletRequest);
        return orderService.findByIdForUser(orderId, authenticatedUser.getUserId());
    }

    private AuthenticatedUser authenticate(HttpServletRequest request) {
        return internalJwtService.authenticate(request.getHeader(accessTokenHeader));
    }
}

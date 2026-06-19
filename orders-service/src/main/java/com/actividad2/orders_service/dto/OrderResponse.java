package com.actividad2.orders_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String userId;
    private String status;
    private String message;
}
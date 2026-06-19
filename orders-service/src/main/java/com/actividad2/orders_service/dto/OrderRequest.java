package com.actividad2.orders_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    @Deprecated
    private String userId;

    @Valid
    @NotEmpty(message = "La orden debe tener al menos un item")
    private List<OrderItemRequest> items;
}

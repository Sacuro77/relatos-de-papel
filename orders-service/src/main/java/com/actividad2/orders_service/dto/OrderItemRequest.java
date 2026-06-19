package com.actividad2.orders_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

    @NotNull(message = "El bookId es obligatorio")
    private Long bookId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer quantity;
}
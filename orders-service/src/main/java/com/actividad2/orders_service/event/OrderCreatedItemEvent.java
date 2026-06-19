package com.actividad2.orders_service.event;

import com.actividad2.orders_service.entity.OrderItem;

import java.math.BigDecimal;

public class OrderCreatedItemEvent {

    private Long bookId;
    private String bookTitle;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public OrderCreatedItemEvent() {
    }

    public OrderCreatedItemEvent(Long bookId, String bookTitle, Integer quantity, BigDecimal unitPrice, BigDecimal subtotal) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    public static OrderCreatedItemEvent from(OrderItem item) {
        return new OrderCreatedItemEvent(
                item.getBookId(),
                item.getBookTitle(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }

    public Long getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}

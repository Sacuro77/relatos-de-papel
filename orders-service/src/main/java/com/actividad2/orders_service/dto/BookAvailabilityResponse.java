package com.actividad2.orders_service.dto;

import java.math.BigDecimal;

public class BookAvailabilityResponse {

    private Long bookId;
    private String title;
    private Boolean exists;
    private Boolean visible;
    private Integer stock;
    private BigDecimal price;
    private Boolean available;
    private String message;

    public BookAvailabilityResponse() {
    }

    public Long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getExists() {
        return exists;
    }

    public Boolean getVisible() {
        return visible;
    }

    public Integer getStock() {
        return stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public String getMessage() {
        return message;
    }
}
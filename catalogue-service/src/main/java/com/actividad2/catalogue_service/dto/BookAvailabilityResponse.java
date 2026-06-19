package com.actividad2.catalogue_service.dto;

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

    public BookAvailabilityResponse(
            Long bookId,
            String title,
            Boolean exists,
            Boolean visible,
            Integer stock,
            BigDecimal price,
            Boolean available,
            String message
    ) {
        this.bookId = bookId;
        this.title = title;
        this.exists = exists;
        this.visible = visible;
        this.stock = stock;
        this.price = price;
        this.available = available;
        this.message = message;
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
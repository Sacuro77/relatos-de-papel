package com.actividad2.catalogue_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private String category;
    private String isbn;
    private Double rating;
    private Boolean visible;
    private Integer stock;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookResponse() {
    }

    public BookResponse(Long id, String title, String author, LocalDate publicationDate, String category,
                        String isbn, Double rating, Boolean visible, Integer stock, BigDecimal price,
                        String description, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.category = category;
        this.isbn = isbn;
        this.rating = rating;
        this.visible = visible;
        this.stock = stock;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String getCategory() {
        return category;
    }

    public String getIsbn() {
        return isbn;
    }

    public Double getRating() {
        return rating;
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

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
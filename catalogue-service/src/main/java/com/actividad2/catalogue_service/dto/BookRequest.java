package com.actividad2.catalogue_service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookRequest {

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    private String author;

    @NotNull(message = "La fecha de publicación es obligatoria")
    private LocalDate publicationDate;

    @NotBlank(message = "La categoría es obligatoria")
    private String category;

    @NotBlank(message = "El ISBN es obligatorio")
    private String isbn;

    @Min(value = 0, message = "La valoración mínima es 0")
    @Max(value = 5, message = "La valoración máxima es 5")
    private Double rating;

    @NotNull(message = "La visibilidad es obligatoria")
    private Boolean visible;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private BigDecimal price;

    private String description;

    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
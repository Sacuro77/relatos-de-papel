package com.actividad2.catalogue_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    @Column(nullable = false)
    private String author;

    @NotNull(message = "La fecha de publicación es obligatoria")
    private LocalDate publicationDate;

    @NotBlank(message = "La categoría es obligatoria")
    @Column(nullable = false)
    private String category;

    @NotBlank(message = "El ISBN es obligatorio")
    @Column(nullable = false, unique = true)
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

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.visible == null) {
            this.visible = true;
        }

        if (this.stock == null) {
            this.stock = 0;
        }

        if (this.rating == null) {
            this.rating = 0.0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
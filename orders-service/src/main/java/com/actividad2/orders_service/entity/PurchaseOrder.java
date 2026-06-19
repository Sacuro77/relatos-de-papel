package com.actividad2.orders_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private BigDecimal total;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = "CREATED";
        }

        if (this.total == null) {
            this.total = BigDecimal.ZERO;
        }
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
package com.actividad2.orders_service.repository;

import com.actividad2.orders_service.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    List<PurchaseOrder> findTop10ByUserIdOrderByCreatedAtDesc(String userId);
}
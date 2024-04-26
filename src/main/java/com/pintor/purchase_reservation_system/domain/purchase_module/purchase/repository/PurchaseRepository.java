package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.repository;

import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.status.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByStatusAndUpdatedAtBetween(PurchaseStatus purchaseStatus, LocalDateTime start, LocalDateTime end);
}

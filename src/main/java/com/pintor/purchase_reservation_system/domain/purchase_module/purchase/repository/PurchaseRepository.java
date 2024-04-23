package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.repository;

import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}

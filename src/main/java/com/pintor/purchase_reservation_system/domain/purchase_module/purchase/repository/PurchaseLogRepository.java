package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.repository;

import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.PurchaseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseLogRepository extends JpaRepository<PurchaseLog, Long> {
}

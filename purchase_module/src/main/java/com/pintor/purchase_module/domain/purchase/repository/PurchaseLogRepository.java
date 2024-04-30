package com.pintor.purchase_module.domain.purchase.repository;

import com.pintor.purchase_module.domain.purchase.entity.PurchaseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseLogRepository extends JpaRepository<PurchaseLog, Long> {
}

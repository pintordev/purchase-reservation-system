package com.pintor.product_module.domain.purchase_module.purchase.repository;

import com.pintor.product_module.domain.purchase_module.purchase.entity.PurchaseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseLogRepository extends JpaRepository<PurchaseLog, Long> {
}

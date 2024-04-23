package com.pintor.purchase_reservation_system.domain.purchase_module.purchase_item.repository;

import com.pintor.purchase_reservation_system.domain.purchase_module.purchase_item.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
}

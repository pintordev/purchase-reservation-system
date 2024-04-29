package com.pintor.purchase_module.domain.purchase_module.purchase_item.repository;

import com.pintor.purchase_module.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.purchase_module.domain.purchase_module.purchase_item.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
    List<PurchaseItem> findAllByPurchaseIdIn(List<Long> purchaseIds);
    List<PurchaseItem> findAllByPurchase(Purchase purchase);
}

package com.pintor.purchase_module.domain.purchase.repository;

import com.pintor.purchase_module.domain.purchase.entity.Purchase;
import com.pintor.purchase_module.domain.purchase.status.PurchaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByStatusAndUpdatedAtBetween(PurchaseStatus purchaseStatus, LocalDateTime start, LocalDateTime end);
    Page<Purchase> findAllByMemberId(Long memberId, Pageable pageable);
    Page<Purchase> findAllByMemberIdAndStatus(Long memberId, PurchaseStatus status, Pageable pageable);
}

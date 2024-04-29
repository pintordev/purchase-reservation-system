package com.pintor.purchase_module.domain.purchase_module.purchase.repository;

import com.pintor.purchase_module.domain.member_module.member.entity.Member;
import com.pintor.purchase_module.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.purchase_module.domain.purchase_module.purchase.status.PurchaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByStatusAndUpdatedAtBetween(PurchaseStatus purchaseStatus, LocalDateTime start, LocalDateTime end);
    Page<Purchase> findAllByMember(Member member, Pageable pageable);
    Page<Purchase> findAllByMemberAndStatus(Member member, PurchaseStatus status, Pageable pageable);
}

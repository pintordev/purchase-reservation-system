package com.pintor.member_module.domain.purchase_module.purchase.service;

import com.pintor.member_module.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.member_module.domain.purchase_module.purchase.entity.PurchaseLog;
import com.pintor.member_module.domain.purchase_module.purchase.repository.PurchaseLogBulkRepository;
import com.pintor.member_module.domain.purchase_module.purchase.repository.PurchaseLogRepository;
import com.pintor.member_module.domain.purchase_module.purchase.status.PurchaseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PurchaseLogService {

    private final PurchaseLogRepository purchaseLogRepository;
    private final PurchaseLogBulkRepository purchaseLogBulkRepository;

    @Transactional
    public void log(Purchase purchase) {

        PurchaseLog purchaseLog = PurchaseLog.builder()
                .purchase(purchase)
                .member(purchase.getMember())
                .status(purchase.getStatus())
                .totalPrice(purchase.getTotalPrice())
                .phoneNumber(purchase.getPhoneNumber())
                .zoneCode(purchase.getZoneCode())
                .address(purchase.getAddress())
                .subAddress(purchase.getSubAddress())
                .createdAt(purchase.getUpdatedAt())
                .build();

        this.purchaseLogRepository.save(purchaseLog);
    }

    @Transactional
    public void log(List<Purchase> purchaseList, PurchaseStatus after, LocalDateTime now) {
        this.purchaseLogBulkRepository.saveAll(purchaseList, after, now);
    }
}

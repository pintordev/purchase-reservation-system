package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.service;

import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.PurchaseLog;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.repository.PurchaseLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PurchaseLogService {

    private final PurchaseLogRepository purchaseLogRepository;

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
}

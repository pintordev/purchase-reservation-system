package com.pintor.purchase_module.common.service;

import com.pintor.purchase_module.domain.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ScheduledJobService {

    private final PurchaseService purchaseService;

    @Scheduled(cron = "* * 16 * * ?")
    public void changePurchaseStatus() {
        log.info("changing PurchaseStatus job started");
        this.purchaseService.changeStatus();
        log.info("changing PurchaseStatus job finished");
    }
}

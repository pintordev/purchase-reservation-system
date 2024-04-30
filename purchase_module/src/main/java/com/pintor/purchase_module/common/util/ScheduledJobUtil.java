package com.pintor.purchase_module.common.util;

import com.pintor.purchase_module.domain.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduledJobUtil {

    private final PurchaseService purchaseService;

    @Scheduled(cron = "0 0 16 * * ?")
    public void changePurchaseStatus() {
        log.info("changing PurchaseStatus job started");
        this.purchaseService.changeStatus();
        log.info("changing PurchaseStatus job finished");
    }
}

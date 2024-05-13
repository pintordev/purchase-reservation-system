package com.pintor.payment_module.api.purchase_module.purchase.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "PURCHASE-MODULE", contextId = "purchaseClient", path = "/api/internal/purchases")
public interface PurchaseClient {
}

package com.pintor.product_module.domain.purchase_module.purchase.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.pintor.product_module.common.response.PagedData;
import com.pintor.product_module.domain.purchase_module.purchase.entity.Purchase;
import lombok.Getter;
import org.springframework.data.domain.Page;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PurchaseListResponse {

    @JsonUnwrapped
    private final PagedData<PurchaseDetailResponse> purchaseList;

    private PurchaseListResponse(Page<Purchase> purchaseList) {
        this.purchaseList = PagedData.of(purchaseList, PurchaseDetailResponse::of);
    }

    public static PurchaseListResponse of(Page<Purchase> purchaseList) {
        return new PurchaseListResponse(purchaseList);
    }
}

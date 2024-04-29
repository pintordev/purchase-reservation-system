package com.pintor.member_module.domain.purchase_module.purchase.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.pintor.member_module.common.response.ListedData;
import com.pintor.member_module.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.member_module.domain.purchase_module.purchase.status.PurchaseStatus;
import com.pintor.member_module.domain.purchase_module.purchase_item.response.PurchaseItemResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PurchaseCreateResponse {

    private final Long id;
    private final PurchaseStatus status;
    private final Integer totalPrice;
    private final String phoneNumber;
    private final String zoneCode;
    private final String address;
    private final String subAddress;
    private final LocalDateTime createdAt;
    @JsonUnwrapped
    private final ListedData<PurchaseItemResponse> purchaseItemList;

    private PurchaseCreateResponse(Purchase purchase) {
        this.id = purchase.getId();
        this.status = purchase.getStatus();
        this.totalPrice = purchase.getTotalPrice();
        this.phoneNumber = purchase.getPhoneNumber();
        this.zoneCode = purchase.getZoneCode();
        this.address = purchase.getAddress();
        this.subAddress = purchase.getSubAddress();
        this.createdAt = purchase.getCreatedAt();
        this.purchaseItemList = ListedData.of(purchase.getPurchaseItemList(), PurchaseItemResponse::of);
    }

    public static PurchaseCreateResponse of(Purchase purchase) {
        return new PurchaseCreateResponse(purchase);
    }
}

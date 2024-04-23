package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.Purchase;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PurchaseCreateResponse {

    public PurchaseCreateResponse(Purchase purchase) {

    }

    public static PurchaseCreateResponse of(Purchase purchase) {
        return new PurchaseCreateResponse(purchase);
    }
}

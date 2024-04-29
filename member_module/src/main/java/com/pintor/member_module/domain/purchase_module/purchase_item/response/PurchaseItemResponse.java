package com.pintor.member_module.domain.purchase_module.purchase_item.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.member_module.domain.purchase_module.purchase_item.entity.PurchaseItem;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PurchaseItemResponse {

    private final Long productId;
    private final String name;
    private final Integer price;
    private final Integer quantity;
    private final Integer totalPrice;

    private PurchaseItemResponse(PurchaseItem purchaseItem) {
        this.productId = purchaseItem.getProduct().getId();
        this.name = purchaseItem.getName();
        this.price = purchaseItem.getPrice();
        this.quantity = purchaseItem.getQuantity();
        this.totalPrice = this.price * this.quantity;
    }

    public static PurchaseItemResponse of(PurchaseItem purchaseItem) {
        return new PurchaseItemResponse(purchaseItem);
    }
}

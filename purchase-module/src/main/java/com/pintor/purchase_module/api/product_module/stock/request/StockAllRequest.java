package com.pintor.purchase_module.api.product_module.stock.request;

import com.pintor.purchase_module.domain.cart_item.entity.CartItem;
import com.pintor.purchase_module.domain.purchase_item.entity.PurchaseItem;

import java.util.List;
import java.util.stream.Collectors;

public record StockAllRequest(List<StockRequest> list) {
    public static StockAllRequest fromCartItem(List<CartItem> cartItemList) {
        List<StockRequest> requestList = cartItemList.stream()
                .map(cartItem -> StockRequest.of(cartItem.getProductId(), cartItem.getQuantity()))
                .collect(Collectors.toList());
        return new StockAllRequest(requestList);
    }

    public static StockAllRequest fromPurchaseItem(List<PurchaseItem> purchaseItemList) {
        List<StockRequest> requestList = purchaseItemList.stream()
                .map(purchaseItem -> StockRequest.of(purchaseItem.getProductId(), purchaseItem.getQuantity()))
                .collect(Collectors.toList());
        return new StockAllRequest(requestList);
    }
}

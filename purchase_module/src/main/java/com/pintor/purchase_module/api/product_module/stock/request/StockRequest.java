package com.pintor.purchase_module.api.product_module.stock.request;

public record StockRequest(Long productId, Integer quantity) {
    public static StockRequest of(Long productId, Integer quantity) {
        return new StockRequest(productId, quantity);
    }
}

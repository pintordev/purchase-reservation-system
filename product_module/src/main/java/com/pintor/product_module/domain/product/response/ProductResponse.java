package com.pintor.product_module.domain.product.response;

import com.pintor.product_module.domain.product.entity.Product;

public record ProductResponse(Long productId, String name, Integer price) {
    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
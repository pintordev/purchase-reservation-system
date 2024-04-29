package com.pintor.purchase_module.domain.cart_item.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {

    private Long productId;
    private String name;
    private Integer price;

    public ProductResponse of(Long productId, String name, Integer price) {
        return new ProductResponse(productId, name, price);
    }
}

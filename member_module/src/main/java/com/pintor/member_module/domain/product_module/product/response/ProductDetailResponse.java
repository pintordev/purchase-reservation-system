package com.pintor.member_module.domain.product_module.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.member_module.domain.product_module.product.entity.Product;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ProductDetailResponse {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String description;
    private final LocalDateTime openedAt;
    private final LocalDateTime createdAt;

    private ProductDetailResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.openedAt = product.getOpenedAt();
        this.createdAt = product.getCreatedAt();
    }

    public static ProductDetailResponse of(Product product) {
        return new ProductDetailResponse(product);
    }
}

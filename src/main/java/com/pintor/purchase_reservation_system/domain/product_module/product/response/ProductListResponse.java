package com.pintor.purchase_reservation_system.domain.product_module.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.pintor.purchase_reservation_system.common.response.PagedData;
import com.pintor.purchase_reservation_system.domain.product_module.product.entity.Product;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ProductListResponse {

    @JsonUnwrapped
    private final PagedData<ProductDetailResponse> productList;

    private ProductListResponse(PagedData<Product> productList) {
        this.productList = productList.map(ProductDetailResponse::of);
    }

    public static ProductListResponse of(PagedData<Product> productList) {
        return new ProductListResponse(productList);
    }
}

package com.pintor.product_module.domain.product_module.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.pintor.product_module.common.response.PagedData;
import com.pintor.product_module.domain.product_module.product.entity.Product;
import lombok.Getter;
import org.springframework.data.domain.Page;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ProductListResponse {

    @JsonUnwrapped
    private final PagedData<ProductDetailResponse> productList;

    private ProductListResponse(Page<Product> productList) {
        this.productList = PagedData.of(productList, ProductDetailResponse::of);
    }

    public static ProductListResponse of(Page<Product> productList) {
        return new ProductListResponse(productList);
    }
}

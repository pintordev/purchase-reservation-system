package com.pintor.purchase_reservation_system.domain.product_module.product.controller;

import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.response.SuccessCode;
import com.pintor.purchase_reservation_system.domain.product_module.product.entity.Product;
import com.pintor.purchase_reservation_system.domain.product_module.product.response.ProductDetailResponse;
import com.pintor.purchase_reservation_system.domain.product_module.product.response.ProductListResponse;
import com.pintor.purchase_reservation_system.domain.product_module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(value = "/api/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity productList(@RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "size", defaultValue = "20") int size,
                                      @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
                                      @RequestParam(value = "dir", defaultValue = "desc") String dir) {

        log.info("product list request: page={}, size={}, sort={}, dir={}", page, size, sort, dir);

        Page<Product> productList = this.productService.getProductList(page, size, sort, dir);

        ResData resData = ResData.of(
                SuccessCode.PRODUCT_LIST,
                ProductListResponse.of(productList)
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity productDetail(@PathVariable(value = "id") Long id) {

            log.info("product detail request: id={}", id);

            Product product = this.productService.getProductDetail(id);

            ResData resData = ResData.of(
                    SuccessCode.PRODUCT_DETAIL,
                    ProductDetailResponse.of(product)
            );
            return ResponseEntity
                    .status(resData.getStatus())
                    .body(resData);
    }
}

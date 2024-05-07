package com.pintor.product_module.domain.product.controller;

import com.pintor.product_module.domain.product.entity.Product;
import com.pintor.product_module.domain.product.response.ProductResponse;
import com.pintor.product_module.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/api/internal/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class InternalProductController {

    private final ProductService productService;

    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ProductResponse getProductDetail(@PathVariable(value = "id") Long id) {
        log.info("product detail request: id={}", id);
        Product product = this.productService.getProductDetail(id);
        return ProductResponse.of(product);
    }
}

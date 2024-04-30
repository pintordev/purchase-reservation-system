package com.pintor.purchase_module.api.product_module.product.client;


import com.pintor.purchase_module.api.product_module.product.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productClient", url = "http://localhost:8082/api/internal/products")
public interface ProductClient {

    @GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ProductResponse getProduct(@PathVariable(value = "id") Long id);
}
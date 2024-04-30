package com.pintor.purchase_module.api.product_module.stock.client;

import com.pintor.purchase_module.api.product_module.stock.request.StockAllRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "stockClient", url = "http://localhost:8082/api/internal/stocks")
public interface StockClient {

    @PostMapping(value = "/decrease/all", consumes = MediaType.APPLICATION_JSON_VALUE)
    void decreaseAllStock(StockAllRequest request);

    @PostMapping(value = "/increase/all", consumes = MediaType.APPLICATION_JSON_VALUE)
    void increaseAllStock(StockAllRequest request);
}

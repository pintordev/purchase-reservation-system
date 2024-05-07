package com.pintor.product_module.domain.stock.controller;

import com.pintor.product_module.domain.stock.request.StockAllRequest;
import com.pintor.product_module.domain.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/api/internal/stocks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class InternalStockController {

    private final StockService stockService;

    @PostMapping(value = "/decrease/all")
    public void decreaseAllStock(@RequestBody StockAllRequest request) {
        log.info("decrease all stock request: {}", request);
        this.stockService.decreaseAll(request);
    }

    @PostMapping(value = "/increase/all")
    public void increaseAllStock(@RequestBody StockAllRequest request) {
        log.info("decrease all stock request: {}", request);
        this.stockService.increaseAll(request);
    }
}

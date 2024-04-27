package com.pintor.purchase_reservation_system.domain.product_module.stock.service;

import com.pintor.purchase_reservation_system.domain.product_module.product.entity.Product;
import com.pintor.purchase_reservation_system.domain.product_module.stock.entity.Stock;
import com.pintor.purchase_reservation_system.domain.product_module.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;

    @Transactional
    public void create(Product product) {

        log.info("create stock: product={}", product);

        Stock stock = Stock.builder()
                .product(product)
                .quantity(1000)
                .build();

        this.stockRepository.save(stock);
    }
}

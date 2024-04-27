package com.pintor.purchase_reservation_system.domain.product_module.stock.service;

import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.domain.product_module.product.entity.Product;
import com.pintor.purchase_reservation_system.domain.product_module.stock.entity.Stock;
import com.pintor.purchase_reservation_system.domain.product_module.stock.repository.StockBulkRepository;
import com.pintor.purchase_reservation_system.domain.product_module.stock.repository.StockRepository;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.entity.CartItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockBulkRepository stockBulkRepository;

    @Transactional
    public void create(Product product) {

        log.info("create stock: product={}", product);

        Stock stock = Stock.builder()
                .product(product)
                .quantity(1000)
                .build();

        this.stockRepository.save(stock);
    }

    @Transactional
    public void decreaseAll(List<CartItem> cartItemList) {

        Map<Long, Integer> quantityMap = new HashMap<>();
        List<Long> productIds = new ArrayList<>();
        cartItemList.stream()
                .forEach(cartItem -> {
                    Long productId = cartItem.getProduct().getId();
                    quantityMap.put(productId, cartItem.getQuantity());
                    productIds.add(productId);
                });

        List<Stock> stockList = this.stockRepository.findAllByProductIdIn(productIds);

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "stockList");

        List<Long> insufficientStockProductIds = new ArrayList<>();
        List<Stock> decreaseStockList = new ArrayList<>();
        stockList.stream()
                .forEach(stock -> {
                    Long productId = stock.getProduct().getId();
                    int decreasedQuantity = stock.getQuantity() - quantityMap.get(productId);
                    if (decreasedQuantity < 0) {
                        insufficientStockProductIds.add(productId);
                        return;
                    }
                    stock = stock.toBuilder()
                            .quantity(decreasedQuantity)
                            .build();
                    decreaseStockList.add(stock);
                });

        if (!insufficientStockProductIds.isEmpty()) {

            bindingResult.rejectValue("productId", "insufficient stock", insufficientStockProductIds.toArray(), "some products have insufficient stock");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INSUFFICIENT_STOCK,
                            bindingResult
                    )
            );
        }

        this.stockBulkRepository.saveAll(decreaseStockList);
    }
}

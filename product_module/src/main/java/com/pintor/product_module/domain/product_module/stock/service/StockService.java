package com.pintor.product_module.domain.product_module.stock.service;

import com.pintor.product_module.common.errors.exception.ApiResException;
import com.pintor.product_module.common.response.FailCode;
import com.pintor.product_module.common.response.ResData;
import com.pintor.product_module.domain.product_module.product.entity.Product;
import com.pintor.product_module.domain.product_module.stock.entity.Stock;
import com.pintor.product_module.domain.product_module.stock.repository.StockBulkRepository;
import com.pintor.product_module.domain.product_module.stock.repository.StockRepository;
import com.pintor.product_module.domain.purchase_module.cart_item.entity.CartItem;
import com.pintor.product_module.domain.purchase_module.purchase_item.entity.PurchaseItem;
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
import java.util.stream.Collectors;

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
        cartItemList.forEach(cartItem -> {
            Long productId = cartItem.getProduct().getId();
            quantityMap.put(productId, cartItem.getQuantity());
            productIds.add(productId);
        });

        List<Stock> stockList = this.stockRepository.findAllByProductIdIn(productIds);

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "stockList");

        List<Long> insufficientStockProductIds = new ArrayList<>();
        List<Stock> decreaseStockList = new ArrayList<>();
        stockList.forEach(stock -> {
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

    @Transactional
    public void decrease(Product product, Integer quantity) {
        Stock stock = this.getStockByProductId(product.getId());
        int decreasedQuantity = stock.getQuantity() - quantity;
        if (decreasedQuantity < 0) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.INSUFFICIENT_STOCK
                    )
            );
        }
        stock = stock.toBuilder()
                .quantity(decreasedQuantity)
                .build();
        this.stockRepository.save(stock);
    }

    private Stock getStockByProductId(Long productId) {
        return this.stockRepository.findByProductId(productId)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.STOCK_NOT_FOUND
                        )
                ));
    }

    @Transactional
    public void increaseAll(List<PurchaseItem> purchaseItemList) {
        Map<Long, Integer> quantityMap = new HashMap<>();
        List<Long> productIds = new ArrayList<>();
        purchaseItemList.forEach(purchaseItem -> {
            Long productId = purchaseItem.getProduct().getId();
            Integer value = quantityMap.putIfAbsent(productId, purchaseItem.getQuantity());
            if (value != null) {
                quantityMap.put(productId, value + purchaseItem.getQuantity());
            } else {
                productIds.add(productId);
            }
        });

        List<Stock> stockList = this.stockRepository.findAllByProductIdIn(productIds);
        stockList = stockList.stream()
                .map(stock -> {
                    Long productId = stock.getProduct().getId();
                    int increasedQuantity = stock.getQuantity() + quantityMap.get(productId);
                    return stock.toBuilder()
                            .quantity(increasedQuantity)
                            .build();
                })
                .collect(Collectors.toList());

        this.stockBulkRepository.saveAll(stockList);
    }
}

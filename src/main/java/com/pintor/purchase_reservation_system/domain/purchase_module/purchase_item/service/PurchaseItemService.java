package com.pintor.purchase_reservation_system.domain.purchase_module.purchase_item.service;

import com.pintor.purchase_reservation_system.domain.product_module.product.entity.Product;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.entity.CartItem;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.request.PurchaseCreateUnitRequest;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase_item.entity.PurchaseItem;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase_item.repository.PurchaseItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;

    @Transactional
    public void createAll(List<CartItem> cartItemList, Purchase purchase, String type) {
        List<PurchaseItem> purchaseItemList = cartItemList.stream()
                .filter(cartItem -> type.equals("all") || cartItem.isSelected())
                .map(cartItem -> this.create(cartItem, purchase))
                .collect(Collectors.toList());

        this.purchaseItemRepository.saveAll(purchaseItemList);
    }

    @Transactional
    private PurchaseItem create(CartItem cartItem, Purchase purchase) {
        return PurchaseItem.builder()
                .purchase(purchase)
                .product(cartItem.getProduct())
                .name(cartItem.getName())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .build();
    }

    @Transactional
    public void create(PurchaseCreateUnitRequest request, Purchase purchase, CartItem cartItem) {
        PurchaseItem purchaseItem = PurchaseItem.builder()
                .purchase(purchase)
                .product(cartItem.getProduct())
                .name(cartItem.getName())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .build();
        this.purchaseItemRepository.save(purchaseItem);
    }

    @Transactional
    public void create(PurchaseCreateUnitRequest request, Purchase purchase, Product product) {
        PurchaseItem purchaseItem = PurchaseItem.builder()
                .purchase(purchase)
                .product(product)
                .name(product.getName())
                .price(product.getPrice())
                .quantity(request.getQuantity())
                .build();
        this.purchaseItemRepository.save(purchaseItem);
    }

    public List<PurchaseItem> getAllByPurchaseList(List<Purchase> purchaseList) {
        List<Long> purchaseIds = purchaseList.stream()
                .map(Purchase::getId)
                .collect(Collectors.toList());
        return this.purchaseItemRepository.findAllByPurchaseIdIn(purchaseIds);
    }

    public List<PurchaseItem> getAllByPurchase(Purchase purchase) {
        return this.purchaseItemRepository.findAllByPurchase(purchase);
    }
}

package com.pintor.purchase_reservation_system.domain.purchase_module.purchase_item.service;

import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.entity.CartItem;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.Purchase;
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
}

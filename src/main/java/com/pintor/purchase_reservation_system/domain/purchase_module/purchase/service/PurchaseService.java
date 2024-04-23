package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.service;

import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart.entity.Cart;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart.service.CartService;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.entity.CartItem;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.service.CartItemService;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.repository.PurchaseRepository;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.request.PurchaseCreateRequest;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.status.PurchaseStatus;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase_item.service.PurchaseItemService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final PurchaseItemService purchaseItemService;
    private final PurchaseLogService purchaseLogService;

    private final EntityManager entityManager;

    private Purchase refresh(Purchase purchase) {
        this.entityManager.flush();
        purchase = this.getPurchaseById(purchase.getId());
        this.entityManager.refresh(purchase);
        return purchase;
    }

    private Purchase getPurchaseById(Long id) {
        return this.purchaseRepository.findById(id)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.PURCHASE_NOT_FOUND
                        )
                ));
    }

    @Transactional
    public Purchase create(PurchaseCreateRequest request, BindingResult bindingResult, User user) {

        Cart cart = this.cartService.getCart(user);
        List<CartItem> cartItemList = this.cartItemService.getAllByCart(cart);
        int totalPrice = cartItemList.stream()
                .mapToInt(cartItem -> cartItem.getPrice() * cartItem.getQuantity())
                .sum();

        Purchase purchase = Purchase.builder()
                .member(cart.getMember())
                .status(PurchaseStatus.PURCHASED)
                .totalPrice(totalPrice)
                .phoneNumber(request.getPhoneNumber())
                .zoneCode(request.getZoneCode())
                .address(request.getAddress())
                .subAddress(request.getSubAddress() == null ? "" : request.getSubAddress())
                .build();

        this.purchaseRepository.save(purchase);

        this.purchaseItemService.createAll(cartItemList, purchase, request.getType());
        this.purchaseLogService.log(purchase);
        this.cartItemService.deleteAll(cartItemList, request.getType());

        return this.refresh(purchase);
    }
}

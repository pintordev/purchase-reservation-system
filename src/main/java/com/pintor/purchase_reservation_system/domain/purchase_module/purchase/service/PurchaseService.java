package com.pintor.purchase_reservation_system.domain.purchase_module.purchase.service;

import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.service.AddressService;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart.entity.Cart;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart.service.CartService;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.entity.CartItem;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.service.CartItemService;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.entity.Purchase;
import com.pintor.purchase_reservation_system.domain.purchase_module.purchase.repository.PurchaseBulkRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseBulkRepository purchaseBulkRepository;

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final PurchaseItemService purchaseItemService;
    private final PurchaseLogService purchaseLogService;
    private final AddressService addressService;

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

        this.createValidate(request, bindingResult);

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

    private void createValidate(PurchaseCreateRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }

        if (!request.getType().equals("all") && !request.getType().equals("selected")) {

            bindingResult.rejectValue("type", "invalid purchase type", "purchase type must be all or selected");

            throw new ApiResException(
                    ResData.of(
                        FailCode.INVALID_PURCHASE_TYPE,
                        bindingResult
                    )
            );
        }

        if (!this.addressService.isValidAddress(request.getZoneCode(), request.getAddress())) {

            bindingResult.rejectValue("address", "invalid address", "invalid address");

            log.error("invalid address: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_ADDRESS,
                            bindingResult
                    )
            );
        }
    }

    @Transactional
    public void changeStatus() {
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0));
        LocalDateTime start = now.minusDays(2);
        LocalDateTime end = now.minusDays(1);

        // 주문 후 D+1에 배송중
        List<Purchase> purchaseList = this.purchaseRepository.findByStatusAndUpdatedAtBetween(PurchaseStatus.PURCHASED, start, end);
        this.purchaseBulkRepository.saveAllWithStatus(purchaseList, PurchaseStatus.ON_DELIVERY, now);
        this.purchaseLogService.log(purchaseList, PurchaseStatus.ON_DELIVERY, now);

        log.info("updated {} purchases to ON_DELIVERY", purchaseList.size());

        // 배송중 후 D+2일에 배송완료
        purchaseList = this.purchaseRepository.findByStatusAndUpdatedAtBetween(PurchaseStatus.ON_DELIVERY, start, end);
        this.purchaseBulkRepository.saveAllWithStatus(purchaseList, PurchaseStatus.DELIVERED, now);
        this.purchaseLogService.log(purchaseList, PurchaseStatus.DELIVERED, now);

        log.info("updated {} purchases to DELIVERED", purchaseList.size());

        // TODO: 반품 신청 후 D+1에 반품완료
    }
}

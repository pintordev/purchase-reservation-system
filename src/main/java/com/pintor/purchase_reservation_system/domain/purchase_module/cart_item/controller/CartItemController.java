package com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.controller;

import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.response.SuccessCode;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.request.CartItemCreateRequest;
import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/api/cartItems", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity cartItemCreate(@Valid @RequestBody CartItemCreateRequest request, BindingResult bindingResult,
                                         @AuthenticationPrincipal User user) {

        log.info("cart item create request: {}", request);

        this.cartItemService.create(request, bindingResult, user);

        ResData resData = ResData.of(
                SuccessCode.CREATE_CART_ITEM
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }
}

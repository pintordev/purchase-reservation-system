package com.pintor.member_module.domain.purchase_module.cart.controller;

import com.pintor.member_module.common.response.ResData;
import com.pintor.member_module.common.response.SuccessCode;
import com.pintor.member_module.domain.purchase_module.cart.entity.Cart;
import com.pintor.member_module.domain.purchase_module.cart.response.CartListResponse;
import com.pintor.member_module.domain.purchase_module.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/api/carts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class CartController {

    private final CartService cartService;

    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity cartList(@AuthenticationPrincipal User user) {

        Cart cart = this.cartService.getCart(user);

        ResData resData = ResData.of(
                SuccessCode.CART_LIST,
                CartListResponse.of(cart)
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }
}

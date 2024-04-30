package com.pintor.purchase_module.domain.cart_item.controller;

import com.pintor.purchase_module.common.principal.MemberPrincipal;
import com.pintor.purchase_module.common.response.ResData;
import com.pintor.purchase_module.common.response.SuccessCode;
import com.pintor.purchase_module.domain.cart_item.request.CartItemCreateRequest;
import com.pintor.purchase_module.domain.cart_item.request.CartItemUpdateRequest;
import com.pintor.purchase_module.domain.cart_item.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(value = "/api/cartItems", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity createCartItem(@Valid @RequestBody CartItemCreateRequest request, BindingResult bindingResult,
                                         @AuthenticationPrincipal MemberPrincipal principal) {

        log.info("cart item create request: {}", request);

        this.cartItemService.create(request, bindingResult, principal);

        ResData resData = ResData.of(
                SuccessCode.CREATE_CART_ITEM
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity updateCartItem(@PathVariable(value = "id") Long id,
                                         @Valid @RequestBody CartItemUpdateRequest request, BindingResult bindingResult,
                                         @AuthenticationPrincipal MemberPrincipal principal) {

        log.info("cart item update request: {}", request);

        this.cartItemService.update(id, request, bindingResult, principal);

        ResData resData = ResData.of(
                SuccessCode.UPDATE_CART_ITEM
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity deleteCartItem(@PathVariable(value = "id") Long id,
                                         @AuthenticationPrincipal MemberPrincipal principal) {

        log.info("cart item delete request: id={}", id);

        this.cartItemService.delete(id, principal);

        ResData resData = ResData.of(
                SuccessCode.DELETE_CART_ITEM
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }
}

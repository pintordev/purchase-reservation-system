package com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.controller;

import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.service.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/api/carts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class CartItemController {

    private final CartItemService cartItemService;
}

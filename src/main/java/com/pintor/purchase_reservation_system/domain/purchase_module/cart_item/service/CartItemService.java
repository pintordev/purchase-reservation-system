package com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.service;

import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
}

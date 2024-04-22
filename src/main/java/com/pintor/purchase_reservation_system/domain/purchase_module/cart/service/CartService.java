package com.pintor.purchase_reservation_system.domain.purchase_module.cart.service;

import com.pintor.purchase_reservation_system.domain.purchase_module.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
}

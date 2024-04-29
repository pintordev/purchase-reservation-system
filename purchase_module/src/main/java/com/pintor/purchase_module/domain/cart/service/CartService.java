package com.pintor.purchase_module.domain.cart.service;

import com.pintor.purchase_module.domain.cart.entity.Cart;
import com.pintor.purchase_module.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;

    public Cart getCart(User user) {

        // TODO: feign client로 member module 호출
        Long memberId = null;

        Cart cart = this.cartRepository.findByMemberId(memberId)
                .orElse(null);

        if (cart == null) {
            cart = this.createCart(memberId);
        }

        return cart;
    }

    @Transactional
    protected Cart createCart(Long memberId) {

        Cart cart = Cart.builder()
                .memberId(memberId)
                .build();

        return this.cartRepository.save(cart);
    }
}

package com.pintor.purchase_module.domain.cart.service;

import com.pintor.purchase_module.common.principal.MemberPrincipal;
import com.pintor.purchase_module.domain.cart.entity.Cart;
import com.pintor.purchase_module.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;

    public Cart getCart(MemberPrincipal principal) {

        Cart cart = this.cartRepository.findByMemberId(principal.getId())
                .orElse(null);

        if (cart == null) {
            cart = this.createCart(principal.getId());
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

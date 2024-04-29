package com.pintor.product_module.domain.purchase_module.cart.service;

import com.pintor.product_module.domain.member_module.member.entity.Member;
import com.pintor.product_module.domain.member_module.member.service.MemberService;
import com.pintor.product_module.domain.purchase_module.cart.entity.Cart;
import com.pintor.product_module.domain.purchase_module.cart.repository.CartRepository;
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
    private final MemberService memberService;

    public Cart getCart(User user) {

        Member member = this.memberService.getMemberByEmail(user.getUsername());

        Cart cart = this.cartRepository.findByMember(member)
                .orElse(null);

        if (cart == null) {
            cart = this.createCart(member);
        }

        return cart;
    }

    @Transactional
    private Cart createCart(Member member) {

        Cart cart = Cart.builder()
                .member(member)
                .build();

        return this.cartRepository.save(cart);
    }
}

package com.pintor.purchase_module.domain.cart.repository;

import com.pintor.member_module.domain.member.entity.Member;
import com.pintor.purchase_module.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMember(Member member);
}
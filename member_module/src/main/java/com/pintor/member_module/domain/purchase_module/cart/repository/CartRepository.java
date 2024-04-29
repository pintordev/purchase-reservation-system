package com.pintor.member_module.domain.purchase_module.cart.repository;

import com.pintor.member_module.domain.member_module.member.entity.Member;
import com.pintor.member_module.domain.purchase_module.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMember(Member member);
}

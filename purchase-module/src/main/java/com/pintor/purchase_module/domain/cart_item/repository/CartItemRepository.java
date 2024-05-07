package com.pintor.purchase_module.domain.cart_item.repository;

import com.pintor.purchase_module.domain.cart.entity.Cart;
import com.pintor.purchase_module.domain.cart_item.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCart(Cart cart);
}
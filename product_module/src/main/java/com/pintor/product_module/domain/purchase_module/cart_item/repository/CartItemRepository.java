package com.pintor.product_module.domain.purchase_module.cart_item.repository;

import com.pintor.product_module.domain.purchase_module.cart.entity.Cart;
import com.pintor.product_module.domain.purchase_module.cart_item.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCart(Cart cart);
}

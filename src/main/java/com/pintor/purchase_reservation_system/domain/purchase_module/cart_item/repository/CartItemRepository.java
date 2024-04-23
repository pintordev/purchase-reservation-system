package com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.repository;

import com.pintor.purchase_reservation_system.domain.purchase_module.cart_item.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

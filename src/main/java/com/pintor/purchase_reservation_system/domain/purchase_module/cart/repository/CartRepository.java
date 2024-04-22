package com.pintor.purchase_reservation_system.domain.purchase_module.cart.repository;

import com.pintor.purchase_reservation_system.domain.purchase_module.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}

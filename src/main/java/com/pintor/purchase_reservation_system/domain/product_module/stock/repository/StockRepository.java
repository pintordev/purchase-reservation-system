package com.pintor.purchase_reservation_system.domain.product_module.stock.repository;

import com.pintor.purchase_reservation_system.domain.product_module.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}

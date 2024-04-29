package com.pintor.purchase_module.domain.product_module.product.repository;

import com.pintor.purchase_module.domain.product_module.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

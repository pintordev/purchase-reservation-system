package com.pintor.purchase_reservation_system.domain.product_module.product.service;

import com.pintor.purchase_reservation_system.domain.product_module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
}

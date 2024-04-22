package com.pintor.purchase_reservation_system.domain.product_module.product.service;

import com.pintor.purchase_reservation_system.common.response.PagedData;
import com.pintor.purchase_reservation_system.domain.product_module.product.entity.Product;
import com.pintor.purchase_reservation_system.domain.product_module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public PagedData<Product> getProductList(int page, int size, String sort, String dir) {

        List<Sort.Order> sorts = new ArrayList<>();
        if (sort.equals("createdAt") && dir.equals("desc")) {
            sorts.add(Sort.Order.desc("created_at"));
        } else if (sort.equals("createAt") && dir.equals("asc")) {
            sorts.add(Sort.Order.asc("created_at"));
        } else if (sort.equals("openedAt") && dir.equals("desc")) {
            sorts.add(Sort.Order.desc("opened_at"));
        } else if (sort.equals("openedAt") && dir.equals("asc")) {
            sorts.add(Sort.Order.asc("opened_at"));
        } else if (sort.equals("price") && dir.equals("desc")) {
            sorts.add(Sort.Order.desc("price"));
        } else if (sort.equals("price") && dir.equals("asc")) {
            sorts.add(Sort.Order.asc("price"));
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sorts));

        return PagedData.of(this.productRepository.findAll(pageable));
    }
}

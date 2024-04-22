package com.pintor.purchase_reservation_system.domain.product_module.product.service;

import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.PagedData;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.domain.product_module.product.entity.Product;
import com.pintor.purchase_reservation_system.domain.product_module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public PagedData<Product> getProductList(int page, int size, String sort, String dir) {

        this.getProductListValidate(page, size, sort, dir);

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.fromString(dir), sort));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sorts));

        return PagedData.of(this.productRepository.findAll(pageable));
    }

    private void getProductListValidate(int page, int size, String sort, String dir) {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "productList");

        if (size < 1) {

            bindingResult.rejectValue("size", "invalid size", "size must be greater than 0");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_SIZE,
                            bindingResult
                    )
            );
        }

        if (page < 1 || this.productRepository.count() / size < page - 1) {

            bindingResult.rejectValue("page", "invalid page", "page does not exist");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_PAGE,
                            bindingResult
                    )
            );
        }

        if (!sort.equals("createdAt") && !sort.equals("openedAt") && !sort.equals("price")) {

            bindingResult.rejectValue("sort", "invalid sort", "sort is invalid");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_SORT,
                            bindingResult
                    )
            );
        }

        if (!dir.equals("asc") && !dir.equals("desc")) {

            bindingResult.rejectValue("dir", "invalid dir", "dir is invalid");

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_SORT,
                            bindingResult
                    )
            );
        }
    }

    public Product getProductDetail(Long id) {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.PRODUCT_NOT_FOUND
                        )
                ));
    }
}

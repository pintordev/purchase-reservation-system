package com.pintor.product_module.domain.product.service;

import com.pintor.product_module.common.errors.exception.ApiResException;
import com.pintor.product_module.common.response.FailCode;
import com.pintor.product_module.common.response.ResData;
import com.pintor.product_module.domain.product.entity.Product;
import com.pintor.product_module.domain.product.repository.ProductRepository;
import com.pintor.product_module.domain.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final StockService stockService;

    public long count() {
        return this.productRepository.count();
    }

    public Page<Product> getProductList(int page, int size, String sort, String dir) {

        this.getProductListValidate(page, size, sort, dir);

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.fromString(dir), sort));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sorts));

        return this.productRepository.findAll(pageable);
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
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "productDetail");
        bindingResult.rejectValue("id", "invalid id", new Object[]{id}, "product does not exist");
        return this.productRepository.findById(id)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.PRODUCT_NOT_FOUND,
                                bindingResult
                        )
                ));
    }

    @Transactional
    public void create(String name, Integer price, String description) {

        Product product = Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .openedAt(LocalDateTime.now())
                .build();

        this.productRepository.save(product);
        this.stockService.create(product);
    }
}

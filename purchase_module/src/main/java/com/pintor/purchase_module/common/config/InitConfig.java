package com.pintor.purchase_module.common.config;

import com.pintor.purchase_module.domain.product_module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class InitConfig {

    private final ProductService productService;

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            if (this.productService.count() > 0) {
                log.info("init config already done");
                return;
            }
            log.info("init config start");

            this.productService.create("product1", 1000, "product1 description");
            this.productService.create("product2", 2000, "product2 description");
        };
    }
}
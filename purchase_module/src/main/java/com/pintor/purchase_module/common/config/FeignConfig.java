package com.pintor.purchase_module.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.pintor.purchase_module.api.member_module.client")
@Configuration
public class FeignConfig {
}

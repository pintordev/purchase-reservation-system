package com.pintor.product_module.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    // product module
    PRODUCT_LIST(HttpStatus.OK, "product list", "상품 목록을 반환합니다"),
    PRODUCT_DETAIL(HttpStatus.OK, "product detail", "상품 상세 정보를 반환합니다"),;

    private HttpStatus status;
    private String code;
    private String message;

    SuccessCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

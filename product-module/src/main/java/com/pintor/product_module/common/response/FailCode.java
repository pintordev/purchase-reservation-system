package com.pintor.product_module.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FailCode {

    BINDING_ERROR(HttpStatus.BAD_REQUEST, "binding error", "요청 값이 올바르지 않습니다"),

    // Auth Fail Code
    FORBIDDEN(HttpStatus.FORBIDDEN, "forbidden", "권한이 없습니다"),

    // List Query Fail Code
    INVALID_SIZE(HttpStatus.BAD_REQUEST, "invalid size", "페이지 당 개수는 1 이상이어야 합니다"),
    INVALID_PAGE(HttpStatus.BAD_REQUEST, "invalid page", "페이지가 존재하지 않습니다"),
    INVALID_SORT(HttpStatus.BAD_REQUEST, "invalid sort", "정렬 기준이 올바르지 않습니다"),

    // Product Fail Code
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "product not found", "상품을 찾을 수 없습니다"),

    // Stock Fail Code
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "insufficient stock", "재고가 부족합니다"),
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "stock not found", "재고를 찾을 수 없습니다"),

    // Server Fail Code
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error", "서버 오류가 발생했습니다"),;

    private HttpStatus status;
    private String code;
    private String message;

    FailCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

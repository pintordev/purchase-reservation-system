package com.pintor.purchase_module.common.response;

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

    // CartItem Fail Code
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "cart item not found", "장바구니 상품을 찾을 수 없습니다"),

    // Purchase Fail Code
    PURCHASE_NOT_FOUND(HttpStatus.NOT_FOUND, "purchase not found", "주문을 찾을 수 없습니다"),
    INVALID_PURCHASE_TYPE(HttpStatus.BAD_REQUEST, "invalid purchase type", "주문 타입이 올바르지 않습니다"),
    PRODUCT_ID_AND_QUANTITY_REQUIRED(HttpStatus.BAD_REQUEST, "product id and quantity required", "상품 id와 수량은 필수입니다"),
    CART_ITEM_ID_REQUIRED(HttpStatus.BAD_REQUEST, "cart item id required", "장바구니 상품 id는 필수입니다"),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "invalid status", "주문 상태가 올바르지 않습니다"),
    INVALID_CANCELLABLE_STATUS(HttpStatus.BAD_REQUEST, "invalid cancellable status", "주문 취소 가능한 상태가 아닙니다"),
    INVALID_RETURNABLE_STATUS(HttpStatus.BAD_REQUEST, "invalid returnable status", "반품 신청 가능한 상태가 아닙니다"),
    INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "invalid address", "올바르지 않은 주소입니다"),
    INVALID_ZONECODE_AND_ADDRESS(HttpStatus.BAD_REQUEST, "invalid zoneCode and address", "zoneCode와 address는 둘 다 채워지거나 둘 다 비워져야 합니다"),
    INVALID_SUBADDRESS(HttpStatus.BAD_REQUEST, "invalid subAddress", "subAddress는 zoneCode와 address가 채워져 있을 때만 채워질 수 있습니다"),

    // Server Fail Code
    MAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "mail send fail", "메일 발송 중 오류가 발생했습니다"),
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

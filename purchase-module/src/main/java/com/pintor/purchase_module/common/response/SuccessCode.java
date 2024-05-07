package com.pintor.purchase_module.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    // purchase module
    CART_LIST(HttpStatus.OK, "cart list", "장바구니 목록을 반환합니다"),
    CREATE_CART_ITEM(HttpStatus.CREATED, "create cart item", "장바구니에 상품을 추가하였습니다"),
    UPDATE_CART_ITEM(HttpStatus.OK, "update cart item", "장바구니 상품을 업데이트하였습니다"),
    DELETE_CART_ITEM(HttpStatus.OK, "delete cart item", "장바구니 상품을 삭제하였습니다"),
    CREATE_PURCHASE(HttpStatus.CREATED, "create purchase", "주문이 완료되었습니다"),
    CREATE_PURCHASE_UNIT(HttpStatus.CREATED, "create purchase unit", "주문이 완료되었습니다"),
    PURCHASE_LIST(HttpStatus.OK, "purchase list", "주문 목록을 반환합니다"),
    PURCHASE_DETAIL(HttpStatus.OK, "purchase detail", "주문 상세 정보를 반환합니다"),
    CANCEL_PURCHASE(HttpStatus.OK, "cancel purchase", "주문을 취소하였습니다"),
    RETURN_PURCHASE(HttpStatus.OK, "return purchase", "반품 신청을 완료하였습니다"),;

    private HttpStatus status;
    private String code;
    private String message;

    SuccessCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

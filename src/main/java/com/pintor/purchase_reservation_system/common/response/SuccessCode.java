package com.pintor.purchase_reservation_system.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    // member module
    SIGNUP(HttpStatus.CREATED, "signup", "회원가입에 성공하였습니다"),
    VERIFY_MAIL(HttpStatus.OK, "verify mail", "이메일 인증에 성공하였습니다"),
    LOGIN(HttpStatus.OK, "login", "인증 토큰을 반환합니다"),
    LOGOUT(HttpStatus.OK, "logout", "로그아웃에 성공하였습니다"),
    LOGOUT_ALL(HttpStatus.OK, "logout all", "모든 기기에서 로그아웃에 성공하였습니다"),
    UPDATE_PROFILE(HttpStatus.OK, "update profile", "프로필 업데이트가 완료되었습니다"),
    UPDATE_PASSWORD(HttpStatus.OK, "update password", "비밀번호 업데이트가 완료되었습니다"),
    RESET_PASSWORD(HttpStatus.OK, "reset password", "가입한 이메일 주소로 임시비밀번호를 발송했습니다"),
    REFRESH_ACCESS_TOKEN(HttpStatus.OK, "refresh access token", "갱신된 인증 토큰을 반환합니다"),

    // product module
    PRODUCT_LIST(HttpStatus.OK, "product list", "상품 목록을 반환합니다"),
    PRODUCT_DETAIL(HttpStatus.OK, "product detail", "상품 상세 정보를 반환합니다"),

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

package com.pintor.purchase_reservation_system.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    SIGNUP(HttpStatus.CREATED, "signup", "회원가입에 성공하였습니다"),
    VERIFY_MAIL(HttpStatus.OK, "verify mail", "이메일 인증에 성공하였습니다"),
    LOGIN(HttpStatus.OK, "login", "인증 토큰을 반환합니다");

    private HttpStatus status;
    private String code;
    private String message;

    SuccessCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

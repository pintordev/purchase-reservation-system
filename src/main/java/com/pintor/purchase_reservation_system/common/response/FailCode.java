package com.pintor.purchase_reservation_system.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FailCode {

    BINDING_ERROR(HttpStatus.BAD_REQUEST, "binding error", "요청 값이 올바르지 않습니다"),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "email duplicated", "이미 사용중인 이메일입니다"),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "password not match", "비밀번호가 일치하지 않습니다"),
    INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "invalid address", "올바르지 않은 주소입니다");

    private HttpStatus status;
    private String code;
    private String message;

    FailCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

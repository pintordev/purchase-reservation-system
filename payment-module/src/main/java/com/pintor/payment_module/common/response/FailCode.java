package com.pintor.payment_module.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FailCode {

    BINDING_ERROR(HttpStatus.BAD_REQUEST, "binding error", "요청 값이 올바르지 않습니다"),

    // Auth Fail Code
    FORBIDDEN(HttpStatus.FORBIDDEN, "forbidden", "권한이 없습니다"),

    // Payment Fail Code

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

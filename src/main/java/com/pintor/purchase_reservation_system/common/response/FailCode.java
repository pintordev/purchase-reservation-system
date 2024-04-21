package com.pintor.purchase_reservation_system.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FailCode {

    BINDING_ERROR(HttpStatus.BAD_REQUEST, "binding error", "요청 값이 올바르지 않습니다"),

    // Member Service Fail Code
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "duplicated email", "이미 사용중인 이메일입니다"),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "password not match", "비밀번호가 일치하지 않습니다"),
    INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "invalid address", "올바르지 않은 주소입니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "member not found", "회원을 찾을 수 없습니다"),

    // Auth Service Fail Code
    INVALID_MAIL_TOKEN(HttpStatus.BAD_REQUEST, "invalid mail token", "유효하지 않은 메일 토큰입니다"),

    // Auth Fail Code
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "expired access token", "인증 토큰이 만료되었습니다"),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "invalid access token", "유효하지 않은 인증 토큰입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized", "인증되지 않은 사용자입니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "forbidden", "권한이 없습니다"),

    // Server Fail Code
    MAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "mail send fail", "메일 발송 중 오류가 발생했습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error", "서버 오류가 발생했습니다");

    private HttpStatus status;
    private String code;
    private String message;

    FailCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

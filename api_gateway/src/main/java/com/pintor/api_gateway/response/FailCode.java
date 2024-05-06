package com.pintor.api_gateway.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FailCode {

    // Auth Fail Code
    ACCESS_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "access token required", "인증 토큰이 필요합니다"),
    INVALID_PREFIX(HttpStatus.BAD_REQUEST, "invalid prefix", "인증 토큰 접두사가 올바르지 않습니다"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "expired access token", "인증 토큰이 만료되었습니다"),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "invalid access token", "유효하지 않은 인증 토큰입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized", "인증되지 않은 사용자입니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "forbidden", "권한이 없습니다"),

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

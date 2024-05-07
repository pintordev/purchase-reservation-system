package com.pintor.member_module.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    // member module
    SIGNUP(HttpStatus.CREATED, "signup", "회원가입에 성공하였습니다"),
    VERIFY_MAIL(HttpStatus.OK, "verify mail", "이메일 인증에 성공하였습니다"),
    LOGIN(HttpStatus.OK, "login", "가입한 이메일 주소로 로그인 인증코드를 발송했습니다"),
    LOGIN_MAIL(HttpStatus.OK, "login mail", "인증 토큰을 반환합니다"),
    LOGOUT(HttpStatus.OK, "logout", "로그아웃에 성공하였습니다"),
    LOGOUT_ALL(HttpStatus.OK, "logout all", "모든 기기에서 로그아웃에 성공하였습니다"),
    UPDATE_PROFILE(HttpStatus.OK, "update profile", "프로필 업데이트가 완료되었습니다"),
    UPDATE_PASSWORD(HttpStatus.OK, "update password", "비밀번호 업데이트가 완료되었습니다"),
    RESET_PASSWORD(HttpStatus.OK, "reset password", "가입한 이메일 주소로 임시비밀번호를 발송했습니다"),
    REFRESH_ACCESS_TOKEN(HttpStatus.OK, "refresh access token", "갱신된 인증 토큰을 반환합니다"),;

    private HttpStatus status;
    private String code;
    private String message;

    SuccessCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

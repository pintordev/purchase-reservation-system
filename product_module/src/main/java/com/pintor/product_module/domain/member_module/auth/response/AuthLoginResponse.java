package com.pintor.product_module.domain.member_module.auth.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class AuthLoginResponse {

    private final String accessToken;
    private final String refreshToken;

    private AuthLoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static AuthLoginResponse of(String accessToken, String refreshToken) {
        return new AuthLoginResponse(accessToken, refreshToken);
    }
}

package com.pintor.product_module.domain.member_module.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class MemberPasswordResetResponse {

    private final String email;
    private final String password;

    private MemberPasswordResetResponse(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static MemberPasswordResetResponse of(String email, String password) {
        return new MemberPasswordResetResponse(email, password);
    }
}

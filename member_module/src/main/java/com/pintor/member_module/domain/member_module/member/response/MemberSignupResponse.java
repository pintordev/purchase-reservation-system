package com.pintor.member_module.domain.member_module.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.member_module.domain.member_module.member.entity.Member;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class MemberSignupResponse {

    private final String email;

    private MemberSignupResponse(Member member) {
        this.email = member.getEmail();
    }

    public static MemberSignupResponse of(Member member) {
        return new MemberSignupResponse(member);
    }
}

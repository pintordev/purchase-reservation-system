package com.pintor.purchase_reservation_system.domain.member_module.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.purchase_reservation_system.domain.member_module.member.dto.MemberDto;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class MemberSignupResponse {

    private final String email;

    private MemberSignupResponse(MemberDto memberDto) {
        this.email = memberDto.getEmail();
    }

    public static MemberSignupResponse of(MemberDto memberDto) {
        return new MemberSignupResponse(memberDto);
    }
}

package com.pintor.member_module.domain.member.response;

public record MemberPrincipalResponse(Long id, String email, String password, String role) {
    public static MemberPrincipalResponse of(Long id, String email, String password, String role) {
        return new MemberPrincipalResponse(id, email, password, role);
    }
}

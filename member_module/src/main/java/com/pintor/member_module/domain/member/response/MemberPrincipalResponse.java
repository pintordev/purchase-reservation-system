package com.pintor.member_module.domain.member.response;

import com.pintor.member_module.common.principal.MemberPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberPrincipalResponse {

    private Long id;
    private String email;
    private String password;
    private String role;

    public static MemberPrincipalResponse of(Long id, String email, String password, String role) {
        return new MemberPrincipalResponse(id, email, password, role);
    }

    public MemberPrincipal toPrincipal() {
        return new MemberPrincipal(this.id, this.email, this.password, this.role);
    }
}

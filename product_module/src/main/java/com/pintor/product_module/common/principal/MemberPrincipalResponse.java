package com.pintor.product_module.common.principal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberPrincipalResponse {

    private Long id;
    private String email;
    private String password;
    private String role;

    public MemberPrincipalResponse of(Long id, String email, String password, String role) {
        return new MemberPrincipalResponse(id, email, password, role);
    }

    public MemberPrincipal toPrincipal() {
        return new MemberPrincipal(this.id, this.email, this.password, this.role);
    }
}

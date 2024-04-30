package com.pintor.purchase_module.api.member_module.response;

import com.pintor.purchase_module.common.principal.MemberPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPrincipalResponse {

    private Long id;
    private String email;
    private String password;
    private String role;

    public MemberPrincipal toPrincipal() {
        return new MemberPrincipal(this.id, this.email, this.password, this.role);
    }
}

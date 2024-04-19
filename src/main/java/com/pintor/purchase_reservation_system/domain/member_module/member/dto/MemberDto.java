package com.pintor.purchase_reservation_system.domain.member_module.member.dto;

import com.pintor.purchase_reservation_system.domain.member_module.member.role.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {

    public Long id;
    public MemberRole role;
    public String email;
    public String name;
    public String address;
    public boolean emailVerified;
}

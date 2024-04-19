package com.pintor.purchase_reservation_system.domain.member_module.member.role;

import lombok.Getter;

@Getter
public enum MemberRole {

    ADMIN("admin"),
    USER("user");

    private String type;

    MemberRole(String type) {
        this.type = type;
    }
}

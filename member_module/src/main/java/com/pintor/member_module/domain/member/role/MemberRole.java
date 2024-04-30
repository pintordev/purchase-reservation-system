package com.pintor.member_module.domain.member.role;

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

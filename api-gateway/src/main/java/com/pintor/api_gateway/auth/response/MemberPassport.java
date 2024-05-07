package com.pintor.api_gateway.auth.response;

public record MemberPassport(Long id, String email, String role) {
    public static MemberPassport of(Long id, String email, String role) {
        return new MemberPassport(id, email, role);
    }
}

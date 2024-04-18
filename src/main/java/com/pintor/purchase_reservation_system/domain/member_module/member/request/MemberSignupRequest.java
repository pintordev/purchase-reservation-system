package com.pintor.purchase_reservation_system.domain.member_module.member.request;

import com.pintor.purchase_reservation_system.common.util.AppUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSignupRequest {

    @NotBlank(message = "Email is required")
    @Pattern(regexp = AppUtil.EMAIL_REGEX, message = "Invalid email format")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = AppUtil.PASSWORD_REGEX, message = "Password must contain at least 8 characters, one uppercase, one lowercase, one number and one special character")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String passwordConfirm;

    @NotBlank(message = "Zone code is required")
    private String zoneCode;

    @NotBlank(message = "Address is required")
    private String address;

    private String subAddress;

    private boolean isAdmin;
}

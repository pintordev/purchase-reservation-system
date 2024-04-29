package com.pintor.product_module.domain.member_module.member.request;

import com.pintor.product_module.common.config.AppConfig;
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
public class MemberPasswordUpdateRequest {

    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Pattern(regexp = AppConfig.PASSWORD_REGEX, message = "Password must contain at least 8 characters, one uppercase, one lowercase, one number and one special character")
    private String newPassword;

    @NotBlank(message = "New password confirmation is required")
    private String newPasswordConfirm;
}

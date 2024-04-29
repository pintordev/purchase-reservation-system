package com.pintor.purchase_module.domain.member_module.member.request;

import com.pintor.purchase_module.common.config.AppConfig;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfileUpdateRequest {

    @Pattern(regexp = AppConfig.PHONE_NUMBER_REGEX, message = "Invalid phone number format")
    private String phoneNumber;

    private String zoneCode;

    private String address;

    private String subAddress;
}

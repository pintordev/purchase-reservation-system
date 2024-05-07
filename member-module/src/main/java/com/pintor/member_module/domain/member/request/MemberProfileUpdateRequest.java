package com.pintor.member_module.domain.member.request;

import com.pintor.member_module.common.config.AppConfig;
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
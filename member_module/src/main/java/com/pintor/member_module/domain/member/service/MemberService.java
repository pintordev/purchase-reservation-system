package com.pintor.member_module.domain.member.service;

import com.pintor.member_module.common.errors.exception.ApiResException;
import com.pintor.member_module.common.response.FailCode;
import com.pintor.member_module.common.response.ResData;
import com.pintor.member_module.common.principal.MemberPrincipal;
import com.pintor.member_module.common.util.AddressUtil;
import com.pintor.member_module.common.util.EncryptUtil;
import com.pintor.member_module.domain.member.repository.MemberRepository;
import com.pintor.member_module.domain.member.request.MemberPasswordUpdateRequest;
import com.pintor.member_module.domain.member.request.MemberSignupRequest;
import com.pintor.member_module.domain.member.response.MemberPasswordResetResponse;
import com.pintor.member_module.domain.member.role.MemberRole;
import com.pintor.member_module.domain.auth.repository.AuthTokenRepository;
import com.pintor.member_module.domain.member.entity.Member;
import com.pintor.member_module.domain.member.request.MemberPasswordResetRequest;
import com.pintor.member_module.domain.member.request.MemberProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.security.SecureRandom;
import java.util.Collections;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthTokenRepository authTokenRepository;

    private final EncryptUtil encryptUtil;
    private final AddressUtil addressUtil;

    public long count() {
        return this.memberRepository.count();
    }

    @Transactional
    public Member signup(MemberSignupRequest request, BindingResult bindingResult) {

        this.signupValidate(request, bindingResult);

        Member member = Member.builder()
                .role(request.isAdmin() ? MemberRole.ADMIN : MemberRole.USER)
                .email(request.getEmail())
                .name(request.getName())
                .password(this.encryptUtil.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .zoneCode(request.getZoneCode())
                .address(request.getAddress())
                .subAddress(request.getSubAddress() == null ? "" : request.getSubAddress())
                .build();

        return this.memberRepository.save(member);
    }

    private void signupValidate(MemberSignupRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            log.error("binding error: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }

        if (isDuplicatedEmail(request.getEmail())) {

            bindingResult.rejectValue("email", "unique violation", "email is already in use");

            log.error("email duplicated: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.DUPLICATED_EMAIL,
                            bindingResult
                    )
            );
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {

            bindingResult.rejectValue("passwordConfirm", "password not match", "passwords do not match");

            log.error("password not match: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.PASSWORD_NOT_MATCH,
                            bindingResult
                    )
            );
        }

        if (!this.addressUtil.isValidAddress(request.getZoneCode(), request.getAddress())) {

            bindingResult.rejectValue("address", "invalid address", "invalid address");

            log.error("invalid address: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_ADDRESS,
                            bindingResult
                    )
            );
        }

    }

    private boolean isDuplicatedEmail(String email) {
        return this.memberRepository.existsByEmail(email);
    }

    @Transactional
    public void verifyEmail(Long memberId) {
        Member member = this.getMemberById(memberId);

        member = member.toBuilder()
                .emailVerified(true)
                .build();

        this.memberRepository.save(member);
    }

    public Member getMemberById(Long memberId) {
        return this.memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.MEMBER_NOT_FOUND
                        )
                ));
    }

    public Member getMemberByEmail(String username) {
        return this.memberRepository.findByEmail(username)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.MEMBER_NOT_FOUND
                        )
                ));
    }

    private Member getMemberByEmailAndNameAndPhoneNumber(String email, String name, String phoneNumber) {
        return this.memberRepository.findByEmailAndNameAndPhoneNumber(email, name, phoneNumber)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.MEMBER_NOT_FOUND
                        )
                ));
    }

    @Transactional
    public void profileUpdate(MemberProfileUpdateRequest request, BindingResult bindingResult, MemberPrincipal principal) {

        this.profileUpdateValidate(request, bindingResult);

        Member member = this.getMemberById(principal.getId());

        member = member.toBuilder()
                .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : member.getPhoneNumber())
                .zoneCode(request.getZoneCode() != null ? request.getZoneCode() : member.getZoneCode())
                .address(request.getAddress() != null ? request.getAddress() : member.getAddress())
                .subAddress(request.getSubAddress() != null ? request.getSubAddress() :
                        request.getZoneCode() != null && request.getAddress() != null ? "" : member.getSubAddress())
                .build();

        this.memberRepository.save(member);
    }

    private void profileUpdateValidate(MemberProfileUpdateRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            log.error("binding error: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }

        if ((request.getZoneCode() != null && request.getAddress() == null)
                || (request.getZoneCode() == null && request.getAddress() != null)) {

            bindingResult.rejectValue("zoneCode", "only one of zoneCode and address is null", "zoneCode and address must be both null or both not null");
            bindingResult.rejectValue("address", "only one of zoneCode and address is null", "zoneCode and address must be both null or both not null");

            log.error("invalid address: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_ZONECODE_AND_ADDRESS,
                            bindingResult
                    )
            );
        }

        if (request.getSubAddress() != null && request.getZoneCode() == null) {

            bindingResult.rejectValue("subAddress", "subAddress without zoneCode and address", "subAddress must be null when zoneCode and address are null");

            log.error("invalid address: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_SUBADDRESS,
                            bindingResult
                    )
            );
        }

        if (request.getZoneCode() != null && !this.addressUtil.isValidAddress(request.getZoneCode(), request.getAddress())) {

            bindingResult.rejectValue("address", "invalid address", "invalid address");

            log.error("invalid address: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_ADDRESS,
                            bindingResult
                    )
            );
        }
    }

    @Transactional
    public void passwordUpdate(MemberPasswordUpdateRequest request, BindingResult bindingResult, MemberPrincipal principal) {

        Member member = this.getMemberById(principal.getId());

        this.passwordUpdateValidate(request, bindingResult, member);

        member = member.toBuilder()
                .password(this.encryptUtil.encode(request.getNewPassword()))
                .build();

        this.memberRepository.save(member);
        this.authTokenRepository.deleteAllById(Collections.singleton(member.getId()));
    }

    private void passwordUpdateValidate(MemberPasswordUpdateRequest request, BindingResult bindingResult, Member member) {

        if (bindingResult.hasErrors()) {

            log.error("binding error: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }

        if (!this.encryptUtil.passwordMatches(request.getOldPassword(), member.getPassword())) {

            bindingResult.rejectValue("oldPassword", "password not match", "passwords do not match");

            log.error("password not match: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.PASSWORD_NOT_MATCH,
                            bindingResult
                    )
            );
        }

        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {

            bindingResult.rejectValue("newPasswordConfirm", "password not match", "passwords do not match");

            log.error("password not match: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.PASSWORD_NOT_MATCH,
                            bindingResult
                    )
            );
        }
    }

    @Transactional
    public MemberPasswordResetResponse resetPassword(MemberPasswordResetRequest request, BindingResult bindingResult) {

        this.resetPasswordValidate(bindingResult);

        Member member = this.getMemberByEmailAndNameAndPhoneNumber(request.getEmail(), request.getName(), request.getPhoneNumber());

        String tempPassword = this.generateTempPassword(16);
        member = member.toBuilder()
                .password(this.encryptUtil.encode(tempPassword))
                .build();

        this.memberRepository.save(member);
        this.authTokenRepository.deleteAllById(Collections.singleton(member.getId()));

        return MemberPasswordResetResponse.of(member.getEmail(), tempPassword);
    }

    private void resetPasswordValidate(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            log.error("binding error: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }
    }

    private String generateTempPassword(int len) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
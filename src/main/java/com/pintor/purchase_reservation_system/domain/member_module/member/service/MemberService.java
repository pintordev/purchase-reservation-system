package com.pintor.purchase_reservation_system.domain.member_module.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.service.EncryptService;
import com.pintor.purchase_reservation_system.domain.member_module.member.entity.Member;
import com.pintor.purchase_reservation_system.domain.member_module.member.repository.MemberRepository;
import com.pintor.purchase_reservation_system.domain.member_module.member.request.MemberPasswordUpdateRequest;
import com.pintor.purchase_reservation_system.domain.member_module.member.request.MemberProfileUpdateRequest;
import com.pintor.purchase_reservation_system.domain.member_module.member.request.MemberSignupRequest;
import com.pintor.purchase_reservation_system.domain.member_module.member.role.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    @Value("${kakao.api.key}")
    private String KAKAO_API_KEY;

    private final MemberRepository memberRepository;

    private final EncryptService encryptService;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Transactional
    public Member signup(MemberSignupRequest request, BindingResult bindingResult) {

        this.signupValidate(request, bindingResult);

        Member member = Member.builder()
                .role(request.isAdmin() ? MemberRole.ADMIN : MemberRole.USER)
                .email(request.getEmail())
                .name(request.getName())
                .password(this.encryptService.encode(request.getPassword()))
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

        if (!isValidAddress(request.getZoneCode(), request.getAddress())) {

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

    private boolean isValidAddress(String zoneCode, String address) {

        String response = this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("dapi.kakao.com")
                        .path("/v2/local/search/address.json")
                        .queryParam("query", address)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + KAKAO_API_KEY)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("kakao response: {}", response);

        try {
            JsonNode root = this.objectMapper.readTree(response);
            JsonNode documents = root.path("documents");
            for (JsonNode document : documents) {
                if (document.path("road_address").path("zone_no").asText().equals(zoneCode)) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("Error parsing response: {}", e.getMessage());
        }

        return false;
    }

    @Transactional
    public void verifyEmail(Long memberId) {
        Member member = this.getMemberById(memberId);

        member = member.toBuilder()
                .emailVerified(true)
                .build();

        this.memberRepository.save(member);
    }

    private Member getMemberById(Long memberId) {
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

    @Transactional
    public void profileUpdate(MemberProfileUpdateRequest request, BindingResult bindingResult, User user) {

        this.profileUpdateValidate(request, bindingResult);

        Member member = this.getMemberByEmail(user.getUsername());

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

        if (request.getSubAddress() != null && (request.getZoneCode() == null || request.getAddress() == null)) {

            bindingResult.rejectValue("subAddress", "subAddress without zoneCode and address", "subAddress must be null when zoneCode and address are null");

            log.error("invalid address: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_SUBADDRESS,
                            bindingResult
                    )
            );
        }

        if (request.getZoneCode() != null && request.getAddress() != null && !isValidAddress(request.getZoneCode(), request.getAddress())) {

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
    public void passwordUpdate(MemberPasswordUpdateRequest request, BindingResult bindingResult, User user) {

        Member member = this.getMemberByEmail(user.getUsername());

        this.passwordUpdateValidate(request, bindingResult, member);

        member = member.toBuilder()
                .password(this.encryptService.encode(request.getNewPassword()))
                .build();

        this.memberRepository.save(member);
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

        if (!this.encryptService.passwordMatches(request.getOldPassword(), member.getPassword())) {

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
}
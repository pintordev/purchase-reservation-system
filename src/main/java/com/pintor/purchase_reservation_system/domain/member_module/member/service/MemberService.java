package com.pintor.purchase_reservation_system.domain.member_module.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.service.EncryptService;
import com.pintor.purchase_reservation_system.domain.member_module.member.dto.MemberDto;
import com.pintor.purchase_reservation_system.domain.member_module.member.entity.Member;
import com.pintor.purchase_reservation_system.domain.member_module.member.repository.MemberRepository;
import com.pintor.purchase_reservation_system.domain.member_module.member.request.MemberSignupRequest;
import com.pintor.purchase_reservation_system.domain.member_module.member.role.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public MemberDto signup(MemberSignupRequest request, BindingResult bindingResult) {

        this.signupValidate(request, bindingResult);

        Member member = Member.builder()
                .role(request.isAdmin() ? MemberRole.ADMIN : MemberRole.USER)
                .email(this.encryptService.encrypt(request.getEmail()))
                .name(this.encryptService.encrypt(request.getName()))
                .password(this.encryptService.encode(request.getPassword()))
                .address(this.encryptService.encrypt(request.getAddress()))
                .build();

        this.memberRepository.save(member);

        return this.toDto(member);
    }

    private MemberDto toDto(Member member) {
        return MemberDto.builder()
                .role(member.getRole())
                .email(this.encryptService.decrypt(member.getEmail()))
                .name(this.encryptService.decrypt(member.getName()))
                .address(this.encryptService.decrypt(member.getAddress()))
                .emailVerified(member.isEmailVerified())
                .build();
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
        String encryptedEmail = this.encryptService.encrypt(email);
        return this.memberRepository.existsByEmail(encryptedEmail);
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
}
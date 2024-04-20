package com.pintor.purchase_reservation_system.domain.member_module.auth.service;

import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.service.EncryptService;
import com.pintor.purchase_reservation_system.domain.member_module.auth.entity.MailToken;
import com.pintor.purchase_reservation_system.domain.member_module.auth.repository.MailTokenRepository;
import com.pintor.purchase_reservation_system.domain.member_module.auth.request.AuthLoginRequest;
import com.pintor.purchase_reservation_system.domain.member_module.auth.response.AuthLoginResponse;
import com.pintor.purchase_reservation_system.domain.member_module.member.entity.Member;
import com.pintor.purchase_reservation_system.domain.member_module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MailTokenRepository mailTokenRepository;
    private final MemberRepository memberRepository;

    private final EncryptService encryptService;

    @Transactional
    public String saveMailToken(Long memberId) {

        String code = this.generateToken(32);

        MailToken mailToken = MailToken.builder()
                .id(code)
                .memberId(memberId)
                .build();

        this.mailTokenRepository.save(mailToken);

        return code;
    }

    private String generateToken(int len) {
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

    public Long verifyMailToken(String code) {
        MailToken mailToken = this.mailTokenRepository.findById(code)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.INVALID_MAIL_TOKEN
                        )
                ));
        return mailToken.getMemberId();
    }

    @Transactional
    public AuthLoginResponse login(AuthLoginRequest request, BindingResult bindingResult) {
        
        Member member = this.loginValidate(request, bindingResult);

        // TODO : Implement login logic
        String accessToken = this.authService.getAccessToken(member.getId());
        String refreshToken = this.authService.getRefreshToken();
        this.authService.saveAuthToken(member.getId(), refreshToken, accessToken);

        return AuthLoginResponse.of(accessToken, refreshToken);
    }

    private Member loginValidate(AuthLoginRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            log.error("binding error: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.BINDING_ERROR,
                            bindingResult
                    )
            );
        }

        Member member = this.memberRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (member == null) {

            bindingResult.rejectValue("email", "member not found from email");

            log.error("member not found from email: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.MEMBER_NOT_FOUND,
                            bindingResult
                    )
            );
        }

        if (!this.encryptService.passwordMatches(request.getPassword(), member.getPassword())) {

            bindingResult.rejectValue("password", "password not match");

            log.error("password not match: {}", bindingResult);

            throw new ApiResException(
                    ResData.of(
                            FailCode.PASSWORD_NOT_MATCH,
                            bindingResult
                    )
            );
        }

        return member;
    }
}

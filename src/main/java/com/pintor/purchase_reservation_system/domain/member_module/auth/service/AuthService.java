package com.pintor.purchase_reservation_system.domain.member_module.auth.service;

import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.service.EncryptService;
import com.pintor.purchase_reservation_system.common.util.JwtUtil;
import com.pintor.purchase_reservation_system.domain.member_module.auth.entity.AuthToken;
import com.pintor.purchase_reservation_system.domain.member_module.auth.entity.MailToken;
import com.pintor.purchase_reservation_system.domain.member_module.auth.repository.AuthTokenRepository;
import com.pintor.purchase_reservation_system.domain.member_module.auth.repository.MailTokenRepository;
import com.pintor.purchase_reservation_system.domain.member_module.auth.request.AuthLoginRequest;
import com.pintor.purchase_reservation_system.domain.member_module.auth.request.AuthVerifyMailRequest;
import com.pintor.purchase_reservation_system.domain.member_module.auth.response.AuthLoginResponse;
import com.pintor.purchase_reservation_system.domain.member_module.member.entity.Member;
import com.pintor.purchase_reservation_system.domain.member_module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.security.SecureRandom;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    @Value("${mail.expiration}")
    private Long mailTokenExpiration;

    @Value("${jwt.expiration.refresh_token}")
    private Long authTokenExpiration;

    private final MailTokenRepository mailTokenRepository;
    private final MemberRepository memberRepository;
    private final AuthTokenRepository authTokenRepository;

    private final EncryptService encryptService;
    private final JwtUtil jwtUtil;

    @Transactional
    public String saveMailToken(Long memberId) {

        String code = this.generateToken(32);

        MailToken mailToken = MailToken.builder()
                .id(code)
                .memberId(memberId)
                .timeToLive(this.mailTokenExpiration)
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

    public Long verifyMailToken(AuthVerifyMailRequest request, BindingResult bindingResult) {

        this.verifyMailTokenValidation(request, bindingResult);

        MailToken mailToken = this.mailTokenRepository.findById(request.getCode())
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.INVALID_MAIL_TOKEN
                        )
                ));

        return mailToken.getMemberId();
    }

    private void verifyMailTokenValidation(AuthVerifyMailRequest request, BindingResult bindingResult) {

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

    @Transactional
    public AuthLoginResponse login(AuthLoginRequest request, BindingResult bindingResult) {

        Member member = this.loginValidate(request, bindingResult);

        String accessToken = this.jwtUtil.genAccessToken(member);
        String refreshToken = this.jwtUtil.genRefreshToken();
        this.saveAuthToken(member, refreshToken, accessToken);

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

        if (!member.isEmailVerified()) {

            log.error("email not verified: {}", request.getEmail());

            throw new ApiResException(
                    ResData.of(
                            FailCode.EMAIL_NOT_VERIFIED
                    )
            );
        }

        return member;
    }

    @Transactional
    private void saveAuthToken(Member member, String refreshToken, String accessToken) {

        AuthToken authToken = AuthToken.builder()
                .id(member.getId())
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .timeToLive(this.authTokenExpiration)
                .build();

        this.authTokenRepository.save(authToken);
    }

    @Transactional
    public void logout(String accessToken) {

        AuthToken authToken = this.authTokenRepository.findByAccessToken(accessToken)
                .orElse(null);

        if (authToken != null) {
            this.authTokenRepository.delete(authToken);
        }
    }
}

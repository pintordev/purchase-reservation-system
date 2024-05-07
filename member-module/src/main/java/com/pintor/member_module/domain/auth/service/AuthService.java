package com.pintor.member_module.domain.auth.service;

import com.pintor.member_module.common.errors.exception.ApiResException;
import com.pintor.member_module.common.response.FailCode;
import com.pintor.member_module.common.response.ResData;
import com.pintor.member_module.common.util.EncryptUtil;
import com.pintor.member_module.common.util.JwtUtil;
import com.pintor.member_module.domain.auth.entity.AuthToken;
import com.pintor.member_module.domain.auth.entity.LoginToken;
import com.pintor.member_module.domain.auth.entity.MailToken;
import com.pintor.member_module.domain.auth.repository.AuthTokenRepository;
import com.pintor.member_module.domain.auth.repository.LoginTokenRepository;
import com.pintor.member_module.domain.auth.repository.MailTokenRepository;
import com.pintor.member_module.domain.auth.request.AuthLoginMailRequest;
import com.pintor.member_module.domain.auth.request.AuthLoginRequest;
import com.pintor.member_module.domain.auth.request.AuthVerifyMailRequest;
import com.pintor.member_module.domain.auth.response.AuthLoginResponse;
import com.pintor.member_module.domain.member.entity.Member;
import com.pintor.member_module.domain.member.repository.MemberRepository;
import com.pintor.member_module.domain.member.response.MemberPrincipalResponse;
import com.pintor.member_module.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.security.SecureRandom;
import java.util.Collections;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    @Value("${mail.expiration}")
    private Long mailTokenExpiration;

    @Value("${jwt.expiration.refresh_token}")
    private Long authTokenExpiration;

    @Value("${login.expiration}")
    private Long loginTokenExpiration;

    private final MailTokenRepository mailTokenRepository;
    private final MemberRepository memberRepository;
    private final AuthTokenRepository authTokenRepository;
    private final LoginTokenRepository loginTokenRepository;

    private final MemberService memberService;

    private final EncryptUtil encryptUtil;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;

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

        this.mailTokenRepository.delete(mailToken);

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
    public Member login(AuthLoginRequest request, BindingResult bindingResult) {
        return this.loginValidate(request, bindingResult);
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

        if (!this.encryptUtil.passwordMatches(request.getPassword(), member.getPassword())) {

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
    public String saveLoginToken(Long memberId) {

        String code = this.generateToken(32);

        LoginToken loginToken = LoginToken.builder()
                .id(code)
                .memberId(memberId)
                .timeToLive(this.loginTokenExpiration)
                .build();

        this.loginTokenRepository.save(loginToken);

        return code;
    }

    @Transactional
    public AuthLoginResponse loginMail(AuthLoginMailRequest request, BindingResult bindingResult) {

        this.loginMailValidate(request, bindingResult);

        LoginToken loginToken = this.loginTokenRepository.findById(request.getCode())
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.INVALID_LOGIN_TOKEN
                        )
                ));

        Member member = this.memberService.getMemberById(loginToken.getMemberId());

        String accessToken = this.jwtUtil.genAccessToken(member);
        String refreshToken = this.jwtUtil.genRefreshToken();
        this.saveAuthToken(member, refreshToken, accessToken);

        this.loginTokenRepository.delete(loginToken);

        return AuthLoginResponse.of(accessToken, refreshToken);
    }

    private void loginMailValidate(AuthLoginMailRequest request, BindingResult bindingResult) {

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
    protected void saveAuthToken(Member member, String refreshToken, String accessToken) {

        AuthToken authToken = AuthToken.builder()
                .id(member.getId())
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .timeToLive(this.authTokenExpiration)
                .build();

        this.authTokenRepository.save(authToken);
    }

    @Transactional
    public void logout(String bearerToken) {

        String accessToken = bearerToken.substring("Bearer ".length());
        AuthToken authToken = this.authTokenRepository.findByAccessToken(accessToken)
                .orElse(null);

        if (authToken != null) {
            this.authTokenRepository.delete(authToken);
        }
    }

    @Transactional
    public void logoutAll(Long memberId) {
        this.authTokenRepository.deleteAllById(Collections.singleton(memberId));
    }

    @Transactional
    public String refreshAccessToken(String refreshToken) {

        AuthToken authToken = this.getAuthTokenByRefreshToken(refreshToken);
        Member member = this.memberService.getMemberById(authToken.getId());

        String accessToken = this.jwtUtil.genAccessToken(member);
        authToken = authToken.toBuilder()
                .accessToken(accessToken)
                .build();
        this.authTokenRepository.save(authToken);

        return accessToken;
    }

    private AuthToken getAuthTokenByRefreshToken(String refreshToken) {

        return this.authTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ApiResException(
                        ResData.of(
                                FailCode.INVALID_REFRESH_TOKEN
                        )
                ));
    }

    public MemberPrincipalResponse getMemberPrincipalByAuthToken(String bearerToken) {

        String accessToken = bearerToken.substring("Bearer ".length());
        Long id = this.jwtUtil.getMemberId(accessToken);

        if (id == -1) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.EXPIRED_ACCESS_TOKEN
                    )
            );
        }

        if (id == -2) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.INVALID_ACCESS_TOKEN
                    )
            );
        }

        if (!this.memberRepository.existsById(id)) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.UNAUTHORIZED
                    )
            );
        }

        return this.memberService.getMemberPrincipal(id);
    }

    public void genServerToken() {
        this.redisTemplate.opsForValue().set("server_token", this.jwtUtil.genRefreshToken());
    }
}
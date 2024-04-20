package com.pintor.purchase_reservation_system.domain.member_module.auth.service;

import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.domain.member_module.auth.entity.MailToken;
import com.pintor.purchase_reservation_system.domain.member_module.auth.repository.MailTokenRepository;
import com.pintor.purchase_reservation_system.domain.member_module.auth.request.AuthLoginRequest;
import com.pintor.purchase_reservation_system.domain.member_module.auth.response.AuthLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.security.SecureRandom;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MailTokenRepository mailTokenRepository;

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

        // TODO : Implement login logic
        String accessToken = this.authService.getAccessToken(member.getId());
        String refreshToken = this.authService.getRefreshToken();
        this.authService.saveAuthToken(member.getId(), refreshToken, accessToken);

        return AuthLoginResponse.of(accessToken, refreshToken);
    }
}

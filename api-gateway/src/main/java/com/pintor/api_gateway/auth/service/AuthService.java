package com.pintor.api_gateway.auth.service;

import com.pintor.api_gateway.auth.response.MemberPassport;
import com.pintor.api_gateway.auth.respository.AuthTokenRepository;
import com.pintor.api_gateway.errors.exception.ApiResException;
import com.pintor.api_gateway.response.FailCode;
import com.pintor.api_gateway.response.ResData;
import com.pintor.api_gateway.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthTokenRepository authTokenRepository;

    private final JwtUtil jwtUtil;

    public MemberPassport getMemberPassport(String bearerToken) {

        String accessToken = this.getMemberPassportValidation(bearerToken);
        return this.jwtUtil.getMemberPassport(accessToken);
    }

    private String getMemberPassportValidation(String bearerToken) {

        if (bearerToken == null) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.ACCESS_TOKEN_REQUIRED
                    )
            );
        }

        if (!bearerToken.startsWith("Bearer ")) {
            throw new ApiResException(
                    ResData.of(
                        FailCode.INVALID_PREFIX
                    )
            );
        }

        String accessToken = bearerToken.substring("Bearer ".length());
        if (!this.authTokenRepository.existsByAccessToken(accessToken)) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.UNAUTHORIZED
                    )
            );
        }

        return accessToken;
    }
}

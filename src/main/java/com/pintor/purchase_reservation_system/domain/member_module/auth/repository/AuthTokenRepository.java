package com.pintor.purchase_reservation_system.domain.member_module.auth.repository;

import com.pintor.purchase_reservation_system.domain.member_module.auth.entity.AuthToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthTokenRepository extends CrudRepository<AuthToken, Long> {
    Optional<AuthToken> findByAccessToken(String accessToken);
    Optional<AuthToken> findByRefreshToken(String refreshToken);
}

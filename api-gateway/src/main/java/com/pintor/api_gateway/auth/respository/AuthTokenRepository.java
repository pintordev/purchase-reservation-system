package com.pintor.api_gateway.auth.respository;

import com.pintor.api_gateway.auth.entity.AuthToken;
import org.springframework.data.repository.CrudRepository;

public interface AuthTokenRepository extends CrudRepository<AuthToken, Long> {
    boolean existsByAccessToken(String accessToken);
}

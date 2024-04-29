package com.pintor.purchase_module.domain.member_module.auth.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "auth_token")
public class AuthToken {

    @Id
    private Long id;

    @Indexed
    private String refreshToken;

    @Indexed
    private String accessToken;

    @TimeToLive
    private Long timeToLive;
}

package com.pintor.purchase_reservation_system.domain.member_module.auth.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "MailToken", timeToLive = 60 * 5)
public class MailToken {

    @Id
    private String id;

    private Long memberId;
}

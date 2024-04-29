package com.pintor.member_module.common.config;

import com.pintor.member_module.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class InitConfig {

    private final MemberService memberService;

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            if (this.memberService.count() > 0) {
                log.info("init config already done");
                return;
            }
            log.info("init config start");

            // TODO: add default member data
        };
    }
}

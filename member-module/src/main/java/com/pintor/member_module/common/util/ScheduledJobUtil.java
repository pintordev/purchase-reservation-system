package com.pintor.member_module.common.util;

import com.pintor.member_module.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduledJobUtil {

    private final AuthService authService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void updateServerToken() {
        log.info("update server token");
        this.authService.genServerToken();
    }
}

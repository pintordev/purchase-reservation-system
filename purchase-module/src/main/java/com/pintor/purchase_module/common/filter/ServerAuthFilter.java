package com.pintor.purchase_module.common.filter;

import com.pintor.purchase_module.common.errors.exception.ApiResException;
import com.pintor.purchase_module.common.response.FailCode;
import com.pintor.purchase_module.common.response.ResData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class ServerAuthFilter extends OncePerRequestFilter {

    private final AntPathRequestMatcher internalRequestMatcher = new AntPathRequestMatcher("/internal/**");
    private final StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.internalRequestMatcher.matches(request)) {
            this.checkServerToken(request);
        }
        filterChain.doFilter(request, response);
    }

    private void checkServerToken(HttpServletRequest request) {

        String serverToken = request.getHeader("X-Server-Token");

        if (serverToken == null) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.FORBIDDEN
                    )
            );
        }

        if (!this.redisTemplate.opsForValue().get("server_token").equals(serverToken)) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.FORBIDDEN
                    )
            );
        }
    }
}

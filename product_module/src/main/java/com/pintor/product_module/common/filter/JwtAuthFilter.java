package com.pintor.product_module.common.filter;

import com.pintor.product_module.common.errors.exception.ApiResException;
import com.pintor.product_module.common.response.FailCode;
import com.pintor.product_module.common.response.ResData;
import com.pintor.product_module.common.util.EndpointEntries;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final EndpointEntries endpointEntries;
    private final StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (this.endpointEntries.isPermitAll(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (this.endpointEntries.isInternal(request)) {
            this.checkServerToken(request, response);
        }
        filterChain.doFilter(request, response);
    }

    private void checkServerToken(HttpServletRequest request, HttpServletResponse response) {

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
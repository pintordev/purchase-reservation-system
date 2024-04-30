package com.pintor.purchase_module.common.filter;

import com.pintor.purchase_module.common.principal.MemberPrincipal;
import com.pintor.purchase_module.common.principal.MemberPrincipalResponse;
import com.pintor.purchase_module.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {

            String accessToken = bearerToken.substring("Bearer ".length());

            Long id = this.jwtUtil.getMemberId(accessToken);
            if (id < 0) {
                request.setAttribute("token_validation_level", id);
            } else {
                MemberPrincipalResponse principalResponse = null;
                // TODO: feign client call to member module
                MemberPrincipal principal = principalResponse.toPrincipal();

                if (principal == null) {
                    request.setAttribute("token_validation_level", -3L);
                } else {
                    this.forceAuthentication(principal);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void forceAuthentication(MemberPrincipal principal) {
        UsernamePasswordAuthenticationToken authenticationToken =
                UsernamePasswordAuthenticationToken.authenticated(principal, null, principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}

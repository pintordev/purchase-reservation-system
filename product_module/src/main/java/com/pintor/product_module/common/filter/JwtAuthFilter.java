package com.pintor.product_module.common.filter;

import com.pintor.product_module.common.util.JwtUtil;
import com.pintor.product_module.domain.member_module.auth.entity.AuthToken;
import com.pintor.product_module.domain.member_module.auth.repository.AuthTokenRepository;
import com.pintor.product_module.domain.member_module.member.entity.Member;
import com.pintor.product_module.domain.member_module.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final AuthTokenRepository authTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {

            String accessToken = bearerToken.substring("Bearer ".length());

            Long id = this.jwtUtil.getMemberId(accessToken);
            if (id < 0) {
                request.setAttribute("token_validation_level", id);
            } else {
                Member member = this.memberRepository.findById(id)
                        .orElse(null);
                AuthToken authToken = this.authTokenRepository.findByAccessToken(accessToken)
                        .orElse(null);

                if (member == null || authToken == null) {
                     request.setAttribute("token_validation_level", -3L);
                } else {
                    this.forceAuthentication(member);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void forceAuthentication(Member member) {

        User user = new User(member.getEmail(), member.getPassword(), member.getAuthorities());

        UsernamePasswordAuthenticationToken authenticationToken =
                UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}

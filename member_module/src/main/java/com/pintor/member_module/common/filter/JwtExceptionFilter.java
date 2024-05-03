package com.pintor.member_module.common.filter;

import com.pintor.member_module.common.errors.exception.ApiResException;
import com.pintor.member_module.common.response.FailCode;
import com.pintor.member_module.common.response.ResData;
import com.pintor.member_module.common.util.AppUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ApiResException e) {
            ResData resData = e.getResData();
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(resData.getStatus().value());
            response.getWriter().write(AppUtil.responseSerialize(resData));
        } catch (Exception e) {
            ResData resData = ResData.of(FailCode.INTERNAL_SERVER_ERROR);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(resData.getStatus().value());
            response.getWriter().write(AppUtil.responseSerialize(resData));
        }
    }
}

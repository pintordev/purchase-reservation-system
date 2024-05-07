package com.pintor.purchase_module.common.filter;

import com.pintor.purchase_module.common.errors.exception.ApiResException;
import com.pintor.purchase_module.common.response.FailCode;
import com.pintor.purchase_module.common.response.ResData;
import com.pintor.purchase_module.common.util.AppUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExceptionHandlingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ApiResException e) {
            ResData resData = e.getResData();
            log.error("[Request Error: {}] {}", resData.getCode(), resData.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(resData.getStatus().value());
            response.getWriter().write(AppUtil.responseSerialize(resData));
        } catch (Exception e) {
            ResData resData = ResData.of(FailCode.INTERNAL_SERVER_ERROR);
            log.error("[Request Error: {}] {}", resData.getCode(), resData.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(resData.getStatus().value());
            response.getWriter().write(AppUtil.responseSerialize(resData));
        }
    }
}
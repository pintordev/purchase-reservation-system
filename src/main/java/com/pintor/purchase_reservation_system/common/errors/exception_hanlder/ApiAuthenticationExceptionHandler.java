package com.pintor.purchase_reservation_system.common.errors.exception_hanlder;

import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import com.pintor.purchase_reservation_system.common.util.AppUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiAuthenticationExceptionHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ResData resData;
        Long id = (Long) request.getAttribute("token_validation_level");
        if (id == null) {
            resData= ResData.of(FailCode.UNAUTHORIZED);
        } else if (id == -1) {
            resData = ResData.of(FailCode.EXPIRED_ACCESS_TOKEN);
        } else if (id == -2) {
            resData = ResData.of(FailCode.INVALID_ACCESS_TOKEN);
        } else {
            resData= ResData.of(FailCode.UNAUTHORIZED);
        }
        request.removeAttribute("token_validation_level");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(resData.getStatus().value());
        response.getWriter().write(AppUtil.responseSerialize(resData));
    }
}

package com.pintor.purchase_module.common.errors.exception_hanlder;

import com.pintor.purchase_module.common.response.FailCode;
import com.pintor.purchase_module.common.response.ResData;
import com.pintor.purchase_module.common.util.AppUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiAuthorizationExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ResData resData = ResData.of(FailCode.FORBIDDEN);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(resData.getStatus().value());
        response.getWriter().write(AppUtil.responseSerialize(resData));
    }
}

package com.pintor.member_module.domain.auth.controller;

import com.pintor.member_module.common.response.ResData;
import com.pintor.member_module.common.response.SuccessCode;
import com.pintor.member_module.common.util.MailUtil;
import com.pintor.member_module.domain.auth.request.AuthLoginMailRequest;
import com.pintor.member_module.domain.auth.request.AuthLoginRequest;
import com.pintor.member_module.domain.auth.request.AuthVerifyMailRequest;
import com.pintor.member_module.domain.auth.response.AuthLoginResponse;
import com.pintor.member_module.domain.auth.service.AuthService;
import com.pintor.member_module.domain.member.entity.Member;
import com.pintor.member_module.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/api/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final MailUtil mailUtil;

    @PostMapping(value = "/verify/mail", consumes = MediaType.ALL_VALUE)
    public ResponseEntity verifyMail(@Valid @RequestBody AuthVerifyMailRequest request, BindingResult bindingResult) {

        Long memberId = this.authService.verifyMailToken(request, bindingResult);
        this.memberService.verifyEmail(memberId);

        ResData resData = ResData.of(
                SuccessCode.VERIFY_MAIL
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@Valid @RequestBody AuthLoginRequest request, BindingResult bindingResult) {

        log.info("login request: {}", request);

        Member member = this.authService.login(request, bindingResult);
        String code = this.authService.saveLoginToken(member.getId());
        this.mailUtil.sendLoginCode(member.getEmail(), code);

        ResData resData = ResData.of(
                SuccessCode.LOGIN
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PostMapping(value = "/login/mail")
    public ResponseEntity loginMail(@Valid @RequestBody AuthLoginMailRequest request, BindingResult bindingResult,
                                    HttpServletResponse response) {

        log.info("login mail request: {}", request);

        AuthLoginResponse authLoginResponse = this.authService.loginMail(request, bindingResult);

        response.setHeader("Authorization", authLoginResponse.getAccessToken());
        response.setHeader("Refresh", authLoginResponse.getRefreshToken());
        ResData resData = ResData.of(
                SuccessCode.LOGIN_MAIL
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PostMapping(value = "/logout", consumes = MediaType.ALL_VALUE)
    public ResponseEntity logout(HttpServletRequest request) {

        String accessToken = request.getHeader("Authorization").substring("Bearer ".length());
        this.authService.logout(accessToken);

        ResData resData = ResData.of(
                SuccessCode.LOGOUT
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PostMapping(value = "/logout/all", consumes = MediaType.ALL_VALUE)
    public ResponseEntity logoutAll(@AuthenticationPrincipal User user) {

        this.authService.logoutAll(user);

        ResData resData = ResData.of(
                SuccessCode.LOGOUT_ALL
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }

    @PostMapping(value = "/refresh", consumes = MediaType.ALL_VALUE)
    public ResponseEntity refreshAccessToken(HttpServletRequest request,
                                             HttpServletResponse response) {

        String refreshToken = request.getHeader("Refresh");
        String accessToken = this.authService.refreshAccessToken(refreshToken);

        response.setHeader("Authorization", accessToken);
        ResData resData = ResData.of(
                SuccessCode.REFRESH_ACCESS_TOKEN
        );
        return ResponseEntity
                .status(resData.getStatus())
                .body(resData);
    }
}

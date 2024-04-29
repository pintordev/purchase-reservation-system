package com.pintor.purchase_reservation_system.common.service;

import com.pintor.purchase_reservation_system.common.errors.exception.ApiResException;
import com.pintor.purchase_reservation_system.common.response.FailCode;
import com.pintor.purchase_reservation_system.common.response.ResData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Async
    private void sendEmail(String email, String title, String content) {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(title);
            helper.setText(content, true);
            this.javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new ApiResException(
                    ResData.of(
                            FailCode.MAIL_SEND_FAIL
                    )
            );
        }
    }

    @Async
    public void sendVerificationCode(String email, String code) {
        String title = "PRS 이메일 인증 링크 발송 메일입니다";
        String content = new StringBuilder("아래 링크로 접속하시면 이메일 인증이 완료됩니다<br>")
                .append("http://localhost:8082/auth/mail?code=")
                .append(code)
                .toString();
        this.sendEmail(email, title, content);
    }

    @Async
    public void sendTempPassword(String email, String password) {
        String title = "PRS 임시비밀번호 발송 메일입니다";
        String content = new StringBuilder("아래 임시비밀번호로 로그인 해주세요<br>")
                .append(password)
                .toString();
        this.sendEmail(email, title, content);
    }

    @Async
    public void sendLoginCode(String email, String code) {
        String title = "PRS 로그인 인증코드 발송 메일입니다";
        String content = new StringBuilder("아래 로그인 인증코드로 로그인 해주세요<br>")
                .append(code)
                .toString();
        this.sendEmail(email, title, content);
    }
}

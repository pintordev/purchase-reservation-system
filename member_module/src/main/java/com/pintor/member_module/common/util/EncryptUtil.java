package com.pintor.member_module.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class EncryptUtil {

    private final AesBytesEncryptor aesBytesEncryptor;
    private final PasswordEncoder passwordEncoder;

    public String encrypt(String value) {
        byte[] encrypted = this.aesBytesEncryptor.encrypt(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String value) {
        byte[] decoded = Base64.getDecoder().decode(value);
        return new String(this.aesBytesEncryptor.decrypt(decoded), StandardCharsets.UTF_8);
    }

    public String encode(String password) {
        return this.passwordEncoder.encode(password);
    }

    public boolean passwordMatches(String password, String encodedPassword) {
        return this.passwordEncoder.matches(password, encodedPassword);
    }
}

package com.pintor.purchase_reservation_system.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class EncryptService {

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
}

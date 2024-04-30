package com.pintor.purchase_module.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class EncryptUtil {

    private final AesBytesEncryptor aesBytesEncryptor;

    public String encrypt(String value) {
        byte[] encrypted = this.aesBytesEncryptor.encrypt(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String value) {
        byte[] decoded = Base64.getDecoder().decode(value);
        return new String(this.aesBytesEncryptor.decrypt(decoded), StandardCharsets.UTF_8);
    }
}

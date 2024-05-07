package com.pintor.purchase_module.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;

@Configuration
public class AppConfig {

    public static final String PHONE_NUMBER_REGEX = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";

    @Value("${aes.secret.key}")
    private String AES_SECRET_KEY;

    @Value("${aes.salt}")
    private String AES_SALT;

    @Bean
    public AesBytesEncryptor aesBytesEncryptor() {
        return new AesBytesEncryptor(AES_SECRET_KEY, AES_SALT);
    }
}

package com.pintor.purchase_reservation_system.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9]+[._-]?[a-zA-Z0-9]+@[a-zA-Z0-9._-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})?$";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=_])(?=\\S+$).{8,}$";
    public static final String PHONE_NUMBER_REGEX = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";

    @Value("${aes.secret.key}")
    private String AES_SECRET_KEY;

    @Value("${aes.salt}")
    private String AES_SALT;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AesBytesEncryptor aesBytesEncryptor() {
        return new AesBytesEncryptor(AES_SECRET_KEY, AES_SALT);
    }
}

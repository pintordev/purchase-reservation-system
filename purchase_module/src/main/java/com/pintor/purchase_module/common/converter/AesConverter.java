package com.pintor.purchase_module.common.converter;

import com.pintor.purchase_module.common.service.EncryptService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Converter
public class AesConverter implements AttributeConverter<String, String> {

    private final EncryptService encryptService;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return this.encryptService.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return this.encryptService.decrypt(dbData);
    }
}

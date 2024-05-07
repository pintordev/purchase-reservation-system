package com.pintor.purchase_module.common.converter;

import com.pintor.purchase_module.common.util.EncryptUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Converter
public class AesConverter implements AttributeConverter<String, String> {

    private final EncryptUtil encryptUtil;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return this.encryptUtil.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return this.encryptUtil.decrypt(dbData);
    }
}

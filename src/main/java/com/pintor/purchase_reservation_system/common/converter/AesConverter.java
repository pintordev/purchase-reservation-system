package com.pintor.purchase_reservation_system.common.converter;

import com.pintor.purchase_reservation_system.common.service.EncryptService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
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

package com.pintor.purchase_reservation_system.domain.member_module.member.converter;

import com.pintor.purchase_reservation_system.common.service.EncryptService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Converter(autoApply = true)
public class MemberConverter implements AttributeConverter<String, String> {

    private final EncryptService encryptService;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return this.encryptService.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return this.encryptService.encrypt(dbData);
    }
}

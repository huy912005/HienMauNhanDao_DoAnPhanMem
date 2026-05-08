package com.Nhom20.DoAnPhamMem.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TheTichTuiMauConverter implements AttributeConverter<TheTichTuiMau, Integer> {
    @Override
    public Integer convertToDatabaseColumn(TheTichTuiMau attribute) {
        return attribute == null ? null : attribute.getMl();
    }

    @Override
    public TheTichTuiMau convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : TheTichTuiMau.fromMl(dbData);
    }
}

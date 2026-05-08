package com.Nhom20.DoAnPhamMem.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TheTichConverter implements AttributeConverter<TheTich, Integer> {
    @Override
    public Integer convertToDatabaseColumn(TheTich attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public TheTich convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : TheTich.fromDbValue(dbData);
    }
}

package com.Nhom20.DoAnPhamMem.converter;

import com.Nhom20.DoAnPhamMem.enums.TheTichTuiMau;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TheTichTuiMauConverter implements AttributeConverter<TheTichTuiMau, Integer> {
    @Override
    public Integer convertToDatabaseColumn(TheTichTuiMau attribute) {
        if (attribute == null) return null;
        return attribute.getMl();
    }

    @Override
    public TheTichTuiMau convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return null;
        return TheTichTuiMau.fromMl(dbData);
    }
}

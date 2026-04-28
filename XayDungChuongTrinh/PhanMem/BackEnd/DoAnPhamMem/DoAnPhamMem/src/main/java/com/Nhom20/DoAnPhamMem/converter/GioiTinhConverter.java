package com.Nhom20.DoAnPhamMem.converter;

import com.Nhom20.DoAnPhamMem.enums.GioiTinh;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GioiTinhConverter implements AttributeConverter<GioiTinh, String> {
    @Override
    public String convertToDatabaseColumn(GioiTinh attribute) {
        if (attribute == null)
            return null;
        return attribute.getDbValue();
    }

    @Override
    public GioiTinh convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        return GioiTinh.fromDbValue(dbData);
    }
}

package com.Nhom20.DoAnPhamMem.converter;

import com.Nhom20.DoAnPhamMem.enums.TrangThaiChienDich;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiChienDichConverter implements AttributeConverter<TrangThaiChienDich, String> {
    @Override
    public String convertToDatabaseColumn(TrangThaiChienDich attribute) {
        if (attribute == null) return null;
        return attribute.getDbValue();
    }

    @Override
    public TrangThaiChienDich convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return TrangThaiChienDich.fromDbValue(dbData);
    }
}

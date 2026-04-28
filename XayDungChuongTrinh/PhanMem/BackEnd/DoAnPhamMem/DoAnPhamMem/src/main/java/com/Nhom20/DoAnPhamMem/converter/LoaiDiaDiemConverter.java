package com.Nhom20.DoAnPhamMem.converter;

import com.Nhom20.DoAnPhamMem.enums.LoaiDiaDiem;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LoaiDiaDiemConverter implements AttributeConverter<LoaiDiaDiem, String> {
    @Override
    public String convertToDatabaseColumn(LoaiDiaDiem attribute) {
        if (attribute == null) return null;
        return attribute.getDbValue();
    }

    @Override
    public LoaiDiaDiem convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return LoaiDiaDiem.fromDbValue(dbData);
    }
}

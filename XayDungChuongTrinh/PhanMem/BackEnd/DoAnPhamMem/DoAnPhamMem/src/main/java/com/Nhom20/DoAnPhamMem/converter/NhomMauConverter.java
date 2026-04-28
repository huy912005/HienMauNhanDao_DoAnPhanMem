package com.Nhom20.DoAnPhamMem.converter;

import com.Nhom20.DoAnPhamMem.enums.NhomMau;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NhomMauConverter implements AttributeConverter<NhomMau, String> {
    @Override
    public String convertToDatabaseColumn(NhomMau attribute) {
        if (attribute == null) return null;
        return attribute.getDbValue();
    }

    @Override
    public NhomMau convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return NhomMau.fromDbValue(dbData);
    }
}

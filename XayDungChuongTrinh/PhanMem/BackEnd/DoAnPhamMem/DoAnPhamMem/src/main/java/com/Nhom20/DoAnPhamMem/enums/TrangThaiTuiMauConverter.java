package com.Nhom20.DoAnPhamMem.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TrangThaiTuiMauConverter implements AttributeConverter<TrangThaiTuiMau, String> {
    @Override
    public String convertToDatabaseColumn(TrangThaiTuiMau attribute) {
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public TrangThaiTuiMau convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TrangThaiTuiMau.fromDbValue(dbData);
    }
}

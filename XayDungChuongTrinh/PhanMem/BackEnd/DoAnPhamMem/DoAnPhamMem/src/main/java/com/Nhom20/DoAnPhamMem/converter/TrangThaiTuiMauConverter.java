package com.Nhom20.DoAnPhamMem.converter;

import com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiTuiMauConverter implements AttributeConverter<TrangThaiTuiMau, String> {
    @Override
    public String convertToDatabaseColumn(TrangThaiTuiMau attribute) {
        if (attribute == null) return null;
        return attribute.getDbValue();
    }

    @Override
    public TrangThaiTuiMau convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return TrangThaiTuiMau.fromDbValue(dbData);
    }
}

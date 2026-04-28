package com.Nhom20.DoAnPhamMem.converter;

import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiDonDangKyConverter implements AttributeConverter<TrangThaiDonDangKy, String> {
    @Override
    public String convertToDatabaseColumn(TrangThaiDonDangKy attribute) {
        if (attribute == null) return null;
        return attribute.getDbValue();
    }

    @Override
    public TrangThaiDonDangKy convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return TrangThaiDonDangKy.fromDbValue(dbData);
    }
}

package com.Nhom20.DoAnPhamMem.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class TrangThaiDonDangKyConverter implements AttributeConverter<TrangThaiDonDangKy, String> {
    @Override
    public String convertToDatabaseColumn(TrangThaiDonDangKy attribute) {
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public TrangThaiDonDangKy convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TrangThaiDonDangKy.fromDbValue(dbData);
    }
}

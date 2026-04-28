package com.Nhom20.DoAnPhamMem.converter;

import com.Nhom20.DoAnPhamMem.enums.TrangThaiThongBao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiThongBaoConverter implements AttributeConverter<TrangThaiThongBao, String> {
    @Override
    public String convertToDatabaseColumn(TrangThaiThongBao attribute) {
        if (attribute == null) return null;
        return attribute.getDbValue();
    }

    @Override
    public TrangThaiThongBao convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return TrangThaiThongBao.fromDbValue(dbData);
    }
}

package com.Nhom20.DoAnPhamMem.converter;

import com.Nhom20.DoAnPhamMem.enums.TrangThaiTinTuc;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiTinTucConverter implements AttributeConverter<TrangThaiTinTuc, String> {
    @Override
    public String convertToDatabaseColumn(TrangThaiTinTuc attribute) {
        if (attribute == null) return null;
        return attribute.getDbValue();
    }

    @Override
    public TrangThaiTinTuc convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return TrangThaiTinTuc.fromDbValue(dbData);
    }
}

package com.Nhom20.DoAnPhamMem.converter;

import com.Nhom20.DoAnPhamMem.enums.LoaiPhieuNhapXuat;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LoaiPhieuNhapXuatConverter implements AttributeConverter<LoaiPhieuNhapXuat, String> {
    @Override
    public String convertToDatabaseColumn(LoaiPhieuNhapXuat attribute) {
        if (attribute == null) return null;
        return attribute.getDbValue();
    }

    @Override
    public LoaiPhieuNhapXuat convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return LoaiPhieuNhapXuat.fromDbValue(dbData);
    }
}

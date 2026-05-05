package com.Nhom20.DoAnPhamMem.converter;

import com.Nhom20.DoAnPhamMem.enums.TheTich;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TheTichConverter implements AttributeConverter<TheTich, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TheTich theTich) {
        if(theTich == null)
            return null;
        return theTich.getValue();
    }

    @Override
    public TheTich convertToEntityAttribute(Integer s) {
        if (s==null)
            return null;
        return TheTich.fromDbValue(s);
    }
}

package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GioiTinh {
    NAM("Nam"),
    NU("Nữ");

    private final String dbValue;

    GioiTinh(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    public static GioiTinh fromDbValue(String value) {
        for (GioiTinh g : values()) {
            if (g.dbValue.equalsIgnoreCase(value)) {
                return g;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy GioiTinh với giá trị: " + value);
    }
}

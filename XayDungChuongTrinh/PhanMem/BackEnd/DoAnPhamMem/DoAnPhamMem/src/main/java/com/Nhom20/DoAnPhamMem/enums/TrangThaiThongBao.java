package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TrangThaiThongBao {
    DA_DOC("Đã đọc"),
    CHUA_DOC("Chưa đọc");

    private final String dbValue;

    TrangThaiThongBao(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    public static TrangThaiThongBao fromDbValue(String value) {
        for (TrangThaiThongBao t : values()) {
            if (t.dbValue.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy TrangThaiThongBao với giá trị: " + value);
    }
}

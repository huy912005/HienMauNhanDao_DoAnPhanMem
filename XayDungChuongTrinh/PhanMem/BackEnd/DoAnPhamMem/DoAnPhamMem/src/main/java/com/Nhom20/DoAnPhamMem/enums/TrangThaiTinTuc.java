package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TrangThaiTinTuc {
    DA_THEM("Đã thêm"),
    DA_XOA("Đã xoá"),
    HET_HAN("Hết hạn");

    private final String dbValue;

    TrangThaiTinTuc(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    public static TrangThaiTinTuc fromDbValue(String value) {
        for (TrangThaiTinTuc t : values()) {
            if (t.dbValue.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy TrangThaiTinTuc với giá trị: " + value);
    }
}

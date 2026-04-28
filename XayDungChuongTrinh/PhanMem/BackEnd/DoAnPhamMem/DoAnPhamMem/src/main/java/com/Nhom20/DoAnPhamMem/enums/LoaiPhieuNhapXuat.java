package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LoaiPhieuNhapXuat {
    NHAP_KHO("Nhập kho"),
    XUAT_KHO("Xuất kho");

    private final String dbValue;

    LoaiPhieuNhapXuat(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    public static LoaiPhieuNhapXuat fromDbValue(String value) {
        for (LoaiPhieuNhapXuat t : values()) {
            if (t.dbValue.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy LoaiPhieuNhapXuat với giá trị: " + value);
    }
}

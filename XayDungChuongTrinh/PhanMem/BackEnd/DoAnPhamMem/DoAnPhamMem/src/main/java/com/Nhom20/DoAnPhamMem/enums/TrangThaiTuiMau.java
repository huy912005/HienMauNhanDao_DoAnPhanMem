package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TrangThaiTuiMau {
    CHO_XET_NGHIEM("Chờ xét nghiệm"),
    YEU_CAU_NHAP_KHO("Yêu cầu nhập kho"),
    NHAP_KHO("Nhập kho"),
    DA_XUAT("Đã xuất"),
    HUY("Hủy");

    private final String dbValue;

    TrangThaiTuiMau(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    public static TrangThaiTuiMau fromDbValue(String value) {
        for (TrangThaiTuiMau t : values()) {
            if (t.dbValue.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy TrangThaiTuiMau với giá trị: " + value);
    }
}

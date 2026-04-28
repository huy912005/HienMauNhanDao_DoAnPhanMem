package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TrangThaiDonDangKy {
    DA_DANG_KY("Đã đăng ký"),
    DA_HIEN("Đã hiến"),
    DA_NHAN_CHUNG_NHAN("Đã nhận chứng nhận"),
    CHUA_HIEN("Chưa hiến");

    private final String dbValue;

    TrangThaiDonDangKy(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    public static TrangThaiDonDangKy fromDbValue(String value) {
        for (TrangThaiDonDangKy t : values()) {
            if (t.dbValue.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy TrangThaiDonDangKy với giá trị: " + value);
    }
}

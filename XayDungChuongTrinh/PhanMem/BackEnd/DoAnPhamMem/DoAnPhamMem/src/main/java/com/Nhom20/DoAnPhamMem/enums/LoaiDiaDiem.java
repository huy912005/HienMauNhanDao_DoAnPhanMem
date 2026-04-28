package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LoaiDiaDiem {
    BENH_VIEN("Bệnh viện"),
    DIEM_CO_DINH("Điểm cố định"),
    DIEM_LUU_DONG("Điểm lưu động");

    private final String dbValue;

    LoaiDiaDiem(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    public static LoaiDiaDiem fromDbValue(String value) {
        for (LoaiDiaDiem l : values()) {
            if (l.dbValue.equalsIgnoreCase(value)) {
                return l;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy LoaiDiaDiem với giá trị: " + value);
    }
}

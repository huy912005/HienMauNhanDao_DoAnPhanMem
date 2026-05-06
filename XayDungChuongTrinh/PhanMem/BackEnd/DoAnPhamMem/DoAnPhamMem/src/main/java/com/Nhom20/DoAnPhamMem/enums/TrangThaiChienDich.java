package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TrangThaiChienDich {

    DANG_LAP_KE_HOACH("Đang lập kế hoạch"),
    DA_PHE_DUYET("Đã phê duyệt"),
    DANG_DIEN_RA("Đang diễn ra"),
    DA_KET_THUC("Đã kết thúc");

    private final String dbValue;

    TrangThaiChienDich(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    public static TrangThaiChienDich fromDbValue(String value) {
        for (TrangThaiChienDich t : values()) {
            if (t.dbValue.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy TrangThaiChienDich với giá trị: " + value);
    }
}

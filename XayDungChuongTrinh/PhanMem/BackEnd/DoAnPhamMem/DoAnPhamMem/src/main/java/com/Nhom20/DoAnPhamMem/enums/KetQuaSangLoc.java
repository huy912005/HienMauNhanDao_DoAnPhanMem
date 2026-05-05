package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum KetQuaSangLoc {
    DAT_YEU_CAU("Đạt yêu cầu"),
    KHONG_DAT("Không đạt");

    private final String dbValue;

    KetQuaSangLoc(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    public static KetQuaSangLoc fromDbValue(String value) {
        for (KetQuaSangLoc k : values()) {
            if (k.dbValue.equalsIgnoreCase(value)) {
                return k;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy KetQuaSangLoc với giá trị: " + value);
    }
}
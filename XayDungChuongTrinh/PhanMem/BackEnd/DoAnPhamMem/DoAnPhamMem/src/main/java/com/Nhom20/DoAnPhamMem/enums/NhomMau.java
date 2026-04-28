package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NhomMau {
    O_POSITIVE("O+"),
    O_NEGATIVE("O-"),
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-");

    private final String dbValue;

    NhomMau(String dbValue) {
        this.dbValue = dbValue;
    }

    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    public static NhomMau fromDbValue(String value) {
        for (NhomMau nm : values()) {
            if (nm.dbValue.equalsIgnoreCase(value)) {
                return nm;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy NhomMau với giá trị: " + value);
    }
}

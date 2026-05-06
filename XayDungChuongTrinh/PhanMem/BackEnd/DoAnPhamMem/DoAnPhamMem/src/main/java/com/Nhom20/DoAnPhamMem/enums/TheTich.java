package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TheTich {
    ML_250(250),
    ML_350(350),
    ML_450(450);
    private final int value;
    TheTich(int value) {
        this.value = value;
    }
    @JsonValue
    public int getValue() {
        return value;
    }
    public static TheTich fromDbValue(int value) {
        for (TheTich t : TheTich.values()) {
            if (t.value == value) {
                return t;
            }
        }
        // Fallback for unknown volumes to prevent 500 error
        return ML_250;
    }
}

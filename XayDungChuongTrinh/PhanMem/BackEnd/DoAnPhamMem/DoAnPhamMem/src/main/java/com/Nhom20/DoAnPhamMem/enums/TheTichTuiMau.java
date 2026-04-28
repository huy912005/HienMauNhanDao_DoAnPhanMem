package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TheTichTuiMau {
    ML_250(250),
    ML_350(350),
    ML_450(450);

    private final int ml;

    TheTichTuiMau(int ml) {
        this.ml = ml;
    }

    @JsonValue
    public int getMl() {
        return ml;
    }

    public static TheTichTuiMau fromMl(int ml) {
        for (TheTichTuiMau t : values()) {
            if (t.ml == ml) {
                return t;
            }
        }
        throw new IllegalArgumentException("Thể tích không hợp lệ: " + ml + "ml. Chỉ chấp nhận 250, 350, 450.");
    }
}

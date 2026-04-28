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

    /**
     * Trả về giá trị chuỗi được lưu trong CSDL và serialize ra JSON.
     */
    @JsonValue
    public String getDbValue() {
        return dbValue;
    }

    /**
     * Tìm enum từ giá trị chuỗi CSDL (dùng khi deserialize từ DB hoặc JSON).
     *
     * @param value chuỗi trạng thái
     * @return enum tương ứng
     * @throws IllegalArgumentException nếu không tìm thấy
     */
    public static TrangThaiChienDich fromDbValue(String value) {
        for (TrangThaiChienDich t : values()) {
            if (t.dbValue.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy TrangThaiChienDich với giá trị: " + value);
    }
}

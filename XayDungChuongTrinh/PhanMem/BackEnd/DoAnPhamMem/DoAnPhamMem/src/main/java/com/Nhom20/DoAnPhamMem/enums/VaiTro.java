package com.Nhom20.DoAnPhamMem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VaiTro {
    QUAN_TRI_HE_THONG("AD", "Quản trị hệ thống"),
    BAC_SI("BS", "Bác sĩ chuyên khoa"),
    NHAN_VIEN_SANG_LOC("NV_SL", "Nhân viên sàng lọc"),
    NHAN_VIEN_THU_NHAN("NV_TN", "Nhân viên thu nhận"),
    NHAN_VIEN_XET_NGHIEM("NV_XN", "Nhân viên xét nghiệm"),
    QUAN_LY_KHO("QLK", "Quản lý kho máu"),
    TINH_NGUYEN_VIEN("TNV", "Tình nguyện viên");

    private final String maVaiTro;
    private final String tenVaiTro;

    VaiTro(String maVaiTro, String tenVaiTro) {
        this.maVaiTro = maVaiTro;
        this.tenVaiTro = tenVaiTro;
    }

    public String getMaVaiTro() {
        return maVaiTro;
    }

    @JsonValue
    public String getTenVaiTro() {
        return tenVaiTro;
    }

    public static VaiTro fromMa(String ma) {
        for (VaiTro v : values()) {
            if (v.maVaiTro.equalsIgnoreCase(ma)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy VaiTro với mã: " + ma);
    }
}

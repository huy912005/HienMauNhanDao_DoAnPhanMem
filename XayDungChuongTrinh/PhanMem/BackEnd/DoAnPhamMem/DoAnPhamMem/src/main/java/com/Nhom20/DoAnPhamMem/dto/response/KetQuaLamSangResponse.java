package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KetQuaLamSangResponse {
    private String maKQ;
    private String maDon;
    private String maNhanVien;
    private String huyetAp;
    private Integer nhipTim;
    private Double canNang;
    private Double nhietDo;
    private Boolean ketQua;
    private String lyDoTuChoi;
}
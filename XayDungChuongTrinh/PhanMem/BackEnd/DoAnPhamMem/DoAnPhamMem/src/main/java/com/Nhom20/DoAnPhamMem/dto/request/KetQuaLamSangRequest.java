package com.Nhom20.DoAnPhamMem.dto.request;

import lombok.Data;

@Data
public class KetQuaLamSangRequest {
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
package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaLamSangResponse {
    private String maKQ;
    private String maDon;
    private String tenTinhNguyenVien;
    private String ngaySinh;
    private String gioiTinh;
    private String nhomMau;
    private String tenChienDich;
    private String tenBacSi;
    private String maBacSi;
    private String huyetAp;
    private Integer nhipTim;
    private Double canNang;
    private Double nhietDo;
    private Boolean ketQua;
    private String lyDoTuChoi;
}

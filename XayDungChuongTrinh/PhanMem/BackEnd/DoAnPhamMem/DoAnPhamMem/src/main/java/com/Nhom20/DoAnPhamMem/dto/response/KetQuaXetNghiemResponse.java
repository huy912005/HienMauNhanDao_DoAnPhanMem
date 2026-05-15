package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaXetNghiemResponse {
    private String maKQ;
    private String maTuiMau;
    private String maNhanVien;
    private String tenNhanVien;
    private String nhomMau;
    private Integer soLanXetNghiem;
    private Boolean ketQua;
    private String moTa;
}

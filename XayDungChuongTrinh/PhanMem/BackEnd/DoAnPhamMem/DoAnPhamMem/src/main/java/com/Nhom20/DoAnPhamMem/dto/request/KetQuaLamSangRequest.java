package com.Nhom20.DoAnPhamMem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KetQuaLamSangRequest {
    private String maDon;
    private String maNhanVien;
    private String huyetAp;
    private Integer nhipTim;
    private Double canNang;
    private Double nhietDo;
    private Boolean ketQua;
    private String lyDoTuChoi;
    private Integer theTichHien;
}

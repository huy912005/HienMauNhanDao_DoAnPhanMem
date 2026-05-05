package com.Nhom20.DoAnPhamMem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoSoSucKhoeResponse {
    private String maHoSo;
    private String maDon;
    private Boolean khangSinh;
    private Boolean truyenNhiem;
    private Boolean dauHong;
    private Boolean coThai;
    private String moTaKhac;
}


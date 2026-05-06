package com.Nhom20.DoAnPhamMem.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoSoSucKhoeRequest {
    @NotBlank(message = "Mã đơn không được để trống")
    private String maDon;
    private String maHoSo;
    private Boolean khangSinh;
    private Boolean truyenNhiem;
    private Boolean dauHong;
    private Boolean coThai;
    private String moTaKhac;
}

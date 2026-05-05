package com.Nhom20.DoAnPhamMem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class KhamSangLocRequest {
    
    @NotBlank(message = "Mã đơn không được để trống")
    private String maDon;
    
    @NotBlank(message = "Huyết áp không được để trống")
    private String huyetAp;
    
    @NotNull(message = "Nhịp tim không được để trống")
    private Integer nhipTim;
    
    @NotNull(message = "Cân nặng không được để trống")
    private Double canNang;
    
    @NotBlank(message = "Kết quả không được để trống")
    private String ketQua;
    
    private Integer theTichHien;
    
    private String maVachTuiMau;
    
    @NotBlank(message = "Mã nhân viên không được để trống")
    private String maNhanVien;
}
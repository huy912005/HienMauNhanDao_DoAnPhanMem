package com.Nhom20.DoAnPhamMem.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String matKhau;
    private String hoTen;
    private String soDienThoai;
    // Thêm các trường khác nếu cần cho TinhNguyenVien hoặc NhanVien
}

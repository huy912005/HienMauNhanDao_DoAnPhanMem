package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.NhanVienResponse;

public interface NhanVienService {
    ApiResponse<NhanVienResponse> getByMaTaiKhoan(String maTaiKhoan);
}

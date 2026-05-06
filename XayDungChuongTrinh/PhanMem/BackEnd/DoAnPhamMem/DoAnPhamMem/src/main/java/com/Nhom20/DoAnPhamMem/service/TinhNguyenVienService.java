package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.TinhNguyenVienRequest;
import com.Nhom20.DoAnPhamMem.dto.response.TinhNguyenVienReSponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TinhNguyenVienService {
    ApiResponse<TinhNguyenVienReSponse> createTinhNguyenVien(TinhNguyenVienRequest tinhNguyenVienRequest);
    // Lưu hoặc cập nhật TNV theo maTaiKhoan; trả về entity đã lưu
    ApiResponse<TinhNguyenVienReSponse> createOrUpdateTinhNguyenVien(TinhNguyenVienRequest request);
    // Lấy TNV theo maTaiKhoan
    ApiResponse<TinhNguyenVienReSponse> getByMaTaiKhoan(String maTaiKhoan);
    ApiResponse<TinhNguyenVienReSponse> getByCccd(String soCCCD);
    ApiResponse<Page<TinhNguyenVienReSponse>> getAll(Pageable pageable);
}


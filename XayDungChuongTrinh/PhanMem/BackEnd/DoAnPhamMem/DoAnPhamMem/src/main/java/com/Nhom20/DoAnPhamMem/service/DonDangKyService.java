package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.DonDangKyRequest;
import com.Nhom20.DoAnPhamMem.dto.response.DonDangKyResponse;

public interface DonDangKyService {
    ApiResponse<DonDangKyResponse> createDonDangKy(DonDangKyRequest request);
    ApiResponse<DonDangKyResponse> checkDaDangKy(String maTNV, String maChienDich);
    ApiResponse<org.springframework.data.domain.Page<DonDangKyResponse>> getAll(org.springframework.data.domain.Pageable pageable);
    ApiResponse<org.springframework.data.domain.Page<DonDangKyResponse>> getByMaTNV(String maTNV, org.springframework.data.domain.Pageable pageable);
    ApiResponse<DonDangKyResponse> getById(String maDon);
    ApiResponse<DonDangKyResponse> updateDonDangKy(String maDon, DonDangKyRequest request);
    ApiResponse<DonDangKyResponse> cancelDonDangKy(String maDon);
    ApiResponse<Void> deleteDonDangKy(String maDon);
}


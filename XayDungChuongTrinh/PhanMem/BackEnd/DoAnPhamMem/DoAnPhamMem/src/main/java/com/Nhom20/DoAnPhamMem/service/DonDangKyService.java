package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.DonDangKyRequest;
import com.Nhom20.DoAnPhamMem.dto.response.DonDangKyResponse;

public interface DonDangKyService {
    ApiResponse<DonDangKyResponse> createDonDangKy(DonDangKyRequest request);
}

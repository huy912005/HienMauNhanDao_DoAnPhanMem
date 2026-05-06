package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.PhuongXaResponse;

import java.util.List;

public interface PhuongXaService {
    ApiResponse<List<PhuongXaResponse>> getAll();
}

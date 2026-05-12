package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.DiaDiemResponse;

import java.util.List;

public interface DiaDiemService {
    ApiResponse<List<DiaDiemResponse>> getAll();
}

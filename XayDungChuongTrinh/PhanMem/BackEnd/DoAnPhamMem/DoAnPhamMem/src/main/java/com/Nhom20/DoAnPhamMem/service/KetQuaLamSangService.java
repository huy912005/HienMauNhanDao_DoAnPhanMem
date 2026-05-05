package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.KetQuaLamSangRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KetQuaLamSangResponse;

import java.util.List;

public interface KetQuaLamSangService {
    ApiResponse<List<KetQuaLamSangResponse>> getAll();
    ApiResponse<KetQuaLamSangResponse> getById(String id);
}
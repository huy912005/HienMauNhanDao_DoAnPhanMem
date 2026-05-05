package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.TuiMauRequest;
import com.Nhom20.DoAnPhamMem.dto.response.TuiMauResponse;

import java.util.List;

public interface TuiMauService {
    ApiResponse<List<TuiMauResponse>> getAll();
    ApiResponse<TuiMauResponse> getById(String id);
    ApiResponse<TuiMauResponse> create(TuiMauRequest request);
    ApiResponse<TuiMauResponse> update(String id, TuiMauRequest request);
    ApiResponse<Void> delete(String id);
}
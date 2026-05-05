package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.KhamSangLocRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KhamSangLocResponse;

import java.util.List;

public interface KhamSangLocService {
    ApiResponse<List<KhamSangLocResponse>> getAll();
    ApiResponse<KhamSangLocResponse> getById(String id);
    ApiResponse<KhamSangLocResponse> create(KhamSangLocRequest request);
    ApiResponse<KhamSangLocResponse> update(String id, KhamSangLocRequest request);
    ApiResponse<Void> delete(String id);
}
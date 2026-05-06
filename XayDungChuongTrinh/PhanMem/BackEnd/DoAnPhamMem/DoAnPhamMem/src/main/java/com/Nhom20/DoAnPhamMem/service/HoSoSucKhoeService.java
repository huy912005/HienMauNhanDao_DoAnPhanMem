package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.HoSoSucKhoeRequest;
import com.Nhom20.DoAnPhamMem.dto.response.HoSoSucKhoeResponse;

import java.util.List;

public interface HoSoSucKhoeService {
    ApiResponse<List<HoSoSucKhoeResponse>> getAll();
    ApiResponse<HoSoSucKhoeResponse> createHoSo(HoSoSucKhoeRequest hoSoSucKhoeRequest);
    ApiResponse<HoSoSucKhoeResponse> getHoSoByMaDon(String maDon);
}

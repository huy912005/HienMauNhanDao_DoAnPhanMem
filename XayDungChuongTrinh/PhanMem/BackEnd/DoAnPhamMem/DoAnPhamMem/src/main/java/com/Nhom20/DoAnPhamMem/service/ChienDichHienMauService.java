package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.ChiendichHienMauResponse;
import java.util.List;

public interface ChienDichHienMauService {
    ApiResponse<List<ChiendichHienMauResponse>> getAll();
}

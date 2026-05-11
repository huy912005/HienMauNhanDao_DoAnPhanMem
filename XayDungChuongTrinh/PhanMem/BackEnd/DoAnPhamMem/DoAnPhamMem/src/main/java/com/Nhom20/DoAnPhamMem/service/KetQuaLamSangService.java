package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.dto.response.KetQuaLamSangResponse;
import com.Nhom20.DoAnPhamMem.dto.response.ScreeningStatsResponse;
import java.util.List;

public interface KetQuaLamSangService {
    List<KetQuaLamSangResponse> getAll();

    ScreeningStatsResponse getStats();

    void save(com.Nhom20.DoAnPhamMem.dto.request.KetQuaLamSangRequest request);

    List<KetQuaLamSangResponse> getWaiting();

    void delete(String maKQ);
}

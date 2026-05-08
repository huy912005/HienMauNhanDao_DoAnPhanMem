package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.dto.response.CollectionStatsResponse;
import com.Nhom20.DoAnPhamMem.dto.response.TuiMauResponse;
import java.util.List;

public interface TuiMauService {
    List<TuiMauResponse> getAll();

    CollectionStatsResponse getStats();

    void delete(String maTuiMau);
    void updateStatus(String maTuiMau, String status);
}

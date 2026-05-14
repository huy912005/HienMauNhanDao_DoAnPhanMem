package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.dto.response.*;
import java.util.List;
import org.springframework.data.domain.Page;

public interface TuiMauService {
    // Dashboard & Inventory (My features)
    DashboardStatsDTO getDashboardStats();

    List<MonthlyCollectionStatDTO> getBloodCollectionByMonth(int year);

    Page<BloodUnitDTO> getBloodUnits(int page, int size, String search, String bloodType);

    void deleteBloodUnit(String maTuiMau);

    BloodUnitDTO scanBloodUnit(String maTuiMau);

    // Develop features
    List<TuiMauResponse> getAll();

    CollectionStatsResponse getStats();

    void delete(String maTuiMau);

    void updateStatus(String maTuiMau, String status);
    
    void updateTuiMau(String maTuiMau, com.Nhom20.DoAnPhamMem.dto.request.TuiMauRequest request);

    void createTuiMau(com.Nhom20.DoAnPhamMem.dto.request.TuiMauRequest request);
}

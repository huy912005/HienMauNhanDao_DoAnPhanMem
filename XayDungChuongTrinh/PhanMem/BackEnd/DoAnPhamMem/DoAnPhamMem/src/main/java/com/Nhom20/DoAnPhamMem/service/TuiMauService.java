package com.Nhom20.DoAnPhamMem.service;

public interface TuiMauService {
    com.Nhom20.DoAnPhamMem.dto.response.DashboardStatsDTO getDashboardStats();
    java.util.List<com.Nhom20.DoAnPhamMem.dto.response.MonthlyCollectionStatDTO> getBloodCollectionByMonth(int year);
    org.springframework.data.domain.Page<com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO> getBloodUnits(int page, int size, String search, String bloodType);
    void deleteBloodUnit(String maTuiMau);
    com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO scanBloodUnit(String maTuiMau);
}

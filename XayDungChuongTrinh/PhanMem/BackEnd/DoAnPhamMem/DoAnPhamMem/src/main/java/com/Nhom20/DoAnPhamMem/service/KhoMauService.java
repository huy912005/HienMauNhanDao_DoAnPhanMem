package com.Nhom20.DoAnPhamMem.service;

public interface KhoMauService {
    java.util.List<com.Nhom20.DoAnPhamMem.dto.response.BloodTypeStatDTO> getInventoryByBloodType();
    java.util.List<com.Nhom20.DoAnPhamMem.dto.response.KhoMauResponse> getAllKhoMau();
}

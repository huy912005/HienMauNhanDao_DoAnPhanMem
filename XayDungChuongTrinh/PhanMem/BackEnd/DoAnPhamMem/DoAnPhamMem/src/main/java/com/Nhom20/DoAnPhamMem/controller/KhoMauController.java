package com.Nhom20.DoAnPhamMem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/khomau")
@lombok.RequiredArgsConstructor
@org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
public class KhoMauController {
    private final com.Nhom20.DoAnPhamMem.service.KhoMauService khoMauService;

    // API lấy data biểu đồ tròn (Tồn kho)
    @org.springframework.web.bind.annotation.GetMapping("/charts/pie")
    public org.springframework.http.ResponseEntity<java.util.List<com.Nhom20.DoAnPhamMem.dto.response.BloodTypeStatDTO>> getInventoryByBloodType() {
        return org.springframework.http.ResponseEntity.ok(khoMauService.getInventoryByBloodType());
    }

    @org.springframework.web.bind.annotation.GetMapping
    public org.springframework.http.ResponseEntity<java.util.List<com.Nhom20.DoAnPhamMem.dto.response.KhoMauResponse>> getAllKhoMau() {
        return org.springframework.http.ResponseEntity.ok(khoMauService.getAllKhoMau());
    }
}

package com.Nhom20.DoAnPhamMem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tuimau")
@lombok.RequiredArgsConstructor
@org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
public class TuiMauController {

    private final com.Nhom20.DoAnPhamMem.service.TuiMauService tuiMauService;

    // 1. API lấy 4 số thống kê trên cùng
    @org.springframework.web.bind.annotation.GetMapping("/stats")
    public org.springframework.http.ResponseEntity<com.Nhom20.DoAnPhamMem.dto.response.DashboardStatsDTO> getDashboardStats() {
        return org.springframework.http.ResponseEntity.ok(tuiMauService.getDashboardStats());
    }

    // 2. API lấy data biểu đồ cột (mặc định lấy năm 2026)
    @org.springframework.web.bind.annotation.GetMapping("/charts/bar")
    public org.springframework.http.ResponseEntity<java.util.List<com.Nhom20.DoAnPhamMem.dto.response.MonthlyCollectionStatDTO>> getBloodCollectionByMonth(
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "2026") int year) {
        return org.springframework.http.ResponseEntity.ok(tuiMauService.getBloodCollectionByMonth(year));
    }

    // 3. API lấy danh sách chi tiết tồn kho (có phân trang và bộ lọc)
    @org.springframework.web.bind.annotation.GetMapping("/blood-units")
    public org.springframework.http.ResponseEntity<org.springframework.data.domain.Page<com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO>> getBloodUnits(
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "0") int page,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "10") int size,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String search,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String bloodType) {
        return org.springframework.http.ResponseEntity.ok(tuiMauService.getBloodUnits(page, size, search, bloodType));
    }

    // 4. API hủy một túi máu
    @org.springframework.web.bind.annotation.DeleteMapping("/blood-units/{maTuiMau}")
    public org.springframework.http.ResponseEntity<String> deleteBloodUnit(@org.springframework.web.bind.annotation.PathVariable String maTuiMau) {
        tuiMauService.deleteBloodUnit(maTuiMau);
        return org.springframework.http.ResponseEntity.ok("Xóa/Hủy túi máu thành công.");
    }

    // 5. API quét lấy thông tin túi máu trước khi nhập
    @org.springframework.web.bind.annotation.GetMapping("/scan/{maTuiMau}")
    public org.springframework.http.ResponseEntity<com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO> scanBloodUnit(@org.springframework.web.bind.annotation.PathVariable String maTuiMau) {
        return org.springframework.http.ResponseEntity.ok(tuiMauService.scanBloodUnit(maTuiMau));
    }
}

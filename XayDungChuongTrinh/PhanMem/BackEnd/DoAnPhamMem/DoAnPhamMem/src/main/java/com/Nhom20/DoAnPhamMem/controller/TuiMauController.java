package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.dto.response.*;
import com.Nhom20.DoAnPhamMem.service.TuiMauService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tuimau")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TuiMauController {

    private final TuiMauService tuiMauService;
    
    @PostMapping
    public ResponseEntity<Map<String, String>> createTuiMau(@RequestBody com.Nhom20.DoAnPhamMem.dto.request.TuiMauRequest request) {
        String maTuiMau = tuiMauService.createTuiMau(request);
        return ResponseEntity.ok(Map.of("maTuiMau", maTuiMau));
    }

    // --- My Features ---

    @GetMapping("/dashboard/stats") // Đổi path một chút để tránh trùng /stats
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(tuiMauService.getDashboardStats());
    }

    @GetMapping("/charts/bar")
    public ResponseEntity<List<MonthlyCollectionStatDTO>> getBloodCollectionByMonth(
            @RequestParam(defaultValue = "2026") int year) {
        return ResponseEntity.ok(tuiMauService.getBloodCollectionByMonth(year));
    }

    @GetMapping("/blood-units")
    public ResponseEntity<Page<BloodUnitDTO>> getBloodUnits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String bloodType,
            @RequestParam(required = false) String maChienDich) {
        return ResponseEntity.ok(tuiMauService.getBloodUnits(page, size, search, bloodType, maChienDich));
    }

    @DeleteMapping("/blood-units/{maTuiMau}")
    public ResponseEntity<String> deleteBloodUnit(@PathVariable String maTuiMau) {
        tuiMauService.deleteBloodUnit(maTuiMau);
        return ResponseEntity.ok("Xóa/Hủy túi máu thành công.");
    }

    @GetMapping("/scan/{maTuiMau}")
    public ResponseEntity<BloodUnitDTO> scanBloodUnit(@PathVariable String maTuiMau) {
        return ResponseEntity.ok(tuiMauService.scanBloodUnit(maTuiMau));
    }

    // --- Expiry Management ---
    @GetMapping("/expiry-management")
    public ResponseEntity<List<BloodExpiryDTO>> getExpiryManagement(
            @RequestParam(defaultValue = "all") String viewMode,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return ResponseEntity.ok(tuiMauService.getExpiryManagementData(viewMode, search));
    }

    @GetMapping("/expiry-stats")
    public ResponseEntity<ExpiryStatsDTO> getExpiryStats() {
        return ResponseEntity.ok(tuiMauService.getExpiryStats());
    }

    @DeleteMapping("/delete-expired")
    public ResponseEntity<String> deleteExpiredUnits() {
        tuiMauService.deleteExpiredUnits();
        return ResponseEntity.ok("Đã tiêu hủy tất cả các túi máu hết hạn thành công.");
    }

    // --- Develop Features ---

    @GetMapping
    public ResponseEntity<List<TuiMauResponse>> getAll() {
        return ResponseEntity.ok(tuiMauService.getAll());
    }

    @GetMapping("/stats") // Giữ nguyên path này cho develop
    public ResponseEntity<CollectionStatsResponse> getStats() {
        return ResponseEntity.ok(tuiMauService.getStats());
    }

    @DeleteMapping("/{maTuiMau}")
    public ResponseEntity<Void> delete(@PathVariable String maTuiMau) {
        tuiMauService.delete(maTuiMau);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{maTuiMau}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable String maTuiMau, @RequestParam String status) {
        System.out.println("CONTROLLER: Cap nhat trang thai tui mau ma [" + maTuiMau + "] trang thai [" + status + "]");
        tuiMauService.updateStatus(maTuiMau, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{maTuiMau}")
    public ResponseEntity<Void> updateTuiMau(@PathVariable String maTuiMau, @RequestBody com.Nhom20.DoAnPhamMem.dto.request.TuiMauRequest request) {
        tuiMauService.updateTuiMau(maTuiMau, request);
        return ResponseEntity.ok().build();
    }
}

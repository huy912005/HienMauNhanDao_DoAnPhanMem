package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.DonDangKyRequest;
import com.Nhom20.DoAnPhamMem.dto.response.DonDangKyResponse;
import com.Nhom20.DoAnPhamMem.service.DonDangKyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dondangky")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DonDangKyController {

    private final DonDangKyService donDangKyService;

    /** Tạo đơn đăng ký mới */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createDonDangKy(@Valid @RequestBody DonDangKyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(donDangKyService.createDonDangKy(request));
    }

    /**
     * Kiểm tra TNV đã đăng ký chiến dịch chưa
     * GET /api/dondangky/check?maTNV=TN00001&maChienDich=CD00001
     */
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<DonDangKyResponse>> checkDaDangKy(
            @RequestParam String maTNV,
            @RequestParam String maChienDich) {
        return ResponseEntity.ok(donDangKyService.checkDaDangKy(maTNV, maChienDich));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return ResponseEntity.ok(donDangKyService.getAll(pageable));
    }

    /**
     * Lấy đơn đăng ký của người dùng theo maTNV
     * GET /api/dondangky/user/{maTNV}?page=0&size=10
     */
    @GetMapping("/user/{maTNV}")
    public ResponseEntity<ApiResponse<?>> getByMaTNV(
            @PathVariable String maTNV,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return ResponseEntity.ok(donDangKyService.getByMaTNV(maTNV, pageable));
    }

    /**
     * Lấy chi tiết đơn đăng ký
     * GET /api/dondangky/{maDon}
     */
    @GetMapping("/{maDon}")
    public ResponseEntity<ApiResponse<DonDangKyResponse>> getById(@PathVariable String maDon) {
        return ResponseEntity.ok(donDangKyService.getById(maDon));
    }

    @PutMapping("/{maDon}")
    public ResponseEntity<ApiResponse<DonDangKyResponse>> update(
            @PathVariable String maDon,
            @RequestBody DonDangKyRequest request) {
        return ResponseEntity.ok(donDangKyService.updateDonDangKy(maDon, request));
    }

    @DeleteMapping("/{maDon}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String maDon) {
        return ResponseEntity.ok(donDangKyService.deleteDonDangKy(maDon));
    }

    @PutMapping("/{maDon}/huy")
    public ResponseEntity<ApiResponse<DonDangKyResponse>> cancel(@PathVariable String maDon) {
        return ResponseEntity.ok(donDangKyService.cancelDonDangKy(maDon));
    }
}

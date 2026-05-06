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
}

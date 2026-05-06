package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.TinhNguyenVienRequest;
import com.Nhom20.DoAnPhamMem.service.TinhNguyenVienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tinhnguyenvien")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TinhNguyenVienController {
    private final TinhNguyenVienService tinhNguyenVienService;
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createTinhNguyenVien(@Valid @RequestBody TinhNguyenVienRequest tinhNguyenVienRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tinhNguyenVienService.createTinhNguyenVien(tinhNguyenVienRequest));
    }
    @PutMapping("/dang-ky")
    public ResponseEntity<ApiResponse<?>> createOrUpdateTinhNguyenVien(@Valid @RequestBody TinhNguyenVienRequest request) {
        return ResponseEntity.ok(tinhNguyenVienService.createOrUpdateTinhNguyenVien(request));
    }
    @GetMapping("/tai-khoan/{maTaiKhoan}")
    public ResponseEntity<ApiResponse<?>> getByMaTaiKhoan(@PathVariable String maTaiKhoan) {
        return ResponseEntity.ok(tinhNguyenVienService.getByMaTaiKhoan(maTaiKhoan));
    }

    @GetMapping("/cccd/{soCCCD}")
    public ResponseEntity<ApiResponse<?>> getByCccd(@PathVariable String soCCCD) {
        return ResponseEntity.ok(tinhNguyenVienService.getByCccd(soCCCD));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return ResponseEntity.ok(tinhNguyenVienService.getAll(pageable));
    }
}

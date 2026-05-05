package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.KhamSangLocRequest;
import com.Nhom20.DoAnPhamMem.service.KhamSangLocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kham-sang-loc")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Hỗ trợ gọi từ frontend HTML/JS
public class KhamSangLocController {

    private final KhamSangLocService khamSangLocService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> luuKetQuaSangLoc(@Valid @RequestBody KhamSangLocRequest request) {
        khamSangLocService.xuLyKetQuaSangLoc(request);
        
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(true)
                .message("Lưu kết quả khám sàng lọc và thu nhận máu thành công!")
                .build();
        return ResponseEntity.ok(response);
    }
}
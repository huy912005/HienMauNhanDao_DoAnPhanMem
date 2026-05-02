package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.DonDangKyRequest;
import com.Nhom20.DoAnPhamMem.service.DonDangKyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dondangky")
@RequiredArgsConstructor
public class DonDangKyController {
    private final DonDangKyService donDangKyService;
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createDonDangKy(@Valid @RequestBody DonDangKyRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(donDangKyService.createDonDangKy(request));
    }
}

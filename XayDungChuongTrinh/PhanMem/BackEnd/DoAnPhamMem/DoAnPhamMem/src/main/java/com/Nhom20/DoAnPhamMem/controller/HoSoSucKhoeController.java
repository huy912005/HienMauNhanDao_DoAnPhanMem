package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.HoSoSucKhoeRequest;
import com.Nhom20.DoAnPhamMem.dto.response.HoSoSucKhoeResponse;
import com.Nhom20.DoAnPhamMem.service.HoSoSucKhoeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hososuckhoe")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HoSoSucKhoeController {
    private final HoSoSucKhoeService hoSoSucKhoeService;
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getHoSoSucKhoe() {
        return ResponseEntity.status(200).body(hoSoSucKhoeService.getAll());
    }
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createHoSo(@Valid @RequestBody HoSoSucKhoeRequest hoSoSucKhoeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hoSoSucKhoeService.createHoSo(hoSoSucKhoeRequest));
    }

    @GetMapping("/don/{maDon}")
    public ResponseEntity<ApiResponse<?>> getByMaDon(@PathVariable String maDon) {
        return ResponseEntity.ok(hoSoSucKhoeService.getHoSoByMaDon(maDon));
    }
}

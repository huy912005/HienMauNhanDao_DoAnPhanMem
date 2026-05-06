package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nhanvien")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NhanVienController {
    
    private final NhanVienService nhanVienService;

    @GetMapping("/tai-khoan/{maTaiKhoan}")
    public ResponseEntity<ApiResponse<?>> getByMaTaiKhoan(@PathVariable String maTaiKhoan) {
        return ResponseEntity.ok(nhanVienService.getByMaTaiKhoan(maTaiKhoan));
    }
}

package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.PhuongXaResponse;
import com.Nhom20.DoAnPhamMem.service.PhuongXaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/phuongxa")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PhuongXaController {
    private final PhuongXaService phuongXaService;
    @GetMapping
    public ResponseEntity<ApiResponse<List<PhuongXaResponse>>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(phuongXaService.getAll());
    }
}

package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.service.DiaDiemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diadiem")
@RequiredArgsConstructor
public class DiaDiemController {
    private final DiaDiemService diaDiemService;
    @GetMapping
    public ApiResponse<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(diaDiemService.getAll()).getBody();
    }
}

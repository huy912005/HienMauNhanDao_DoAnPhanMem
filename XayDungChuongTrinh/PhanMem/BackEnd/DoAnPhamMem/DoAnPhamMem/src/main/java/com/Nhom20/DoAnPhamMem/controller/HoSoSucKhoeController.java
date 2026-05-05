package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.HoSoSucKhoeResponse;
import com.Nhom20.DoAnPhamMem.service.HoSoSucKhoeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hososuckhoe")
@RequiredArgsConstructor
public class HoSoSucKhoeController {
    private final HoSoSucKhoeService hoSoSucKhoeService;
    @GetMapping
    public ApiResponse<?> getHoSoSucKhoe() {
        return ResponseEntity.status(200).body(hoSoSucKhoeService.getAll()).getBody();
    }
}

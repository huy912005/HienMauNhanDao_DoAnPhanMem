package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.dto.TrangChuResponse;
import com.Nhom20.DoAnPhamMem.service.TrangChuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/trang-chu")
@CrossOrigin(origins = "*")
public class TrangChuController {

    private final TrangChuService trangChuService;

    public TrangChuController(TrangChuService trangChuService) {
        this.trangChuService = trangChuService;
    }

    @GetMapping
    public ResponseEntity<TrangChuResponse> getTrangChu() {
        return ResponseEntity.ok(trangChuService.layDuLieuTrangChu());
    }
}

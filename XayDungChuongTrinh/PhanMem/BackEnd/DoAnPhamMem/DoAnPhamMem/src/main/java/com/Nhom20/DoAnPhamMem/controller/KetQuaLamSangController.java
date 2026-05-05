package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.KetQuaLamSangRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KetQuaLamSangResponse;
import com.Nhom20.DoAnPhamMem.service.KetQuaLamSangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ketqualamsang")
@CrossOrigin(origins = "*") 
public class KetQuaLamSangController {

    @Autowired
    private KetQuaLamSangService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<KetQuaLamSangResponse>>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KetQuaLamSangResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
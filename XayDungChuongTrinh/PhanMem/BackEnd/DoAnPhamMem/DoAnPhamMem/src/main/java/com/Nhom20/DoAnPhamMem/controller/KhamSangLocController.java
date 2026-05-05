package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.KhamSangLocRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KhamSangLocResponse;
import com.Nhom20.DoAnPhamMem.service.KhamSangLocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khamsangloc")
@CrossOrigin(origins = "*") 
@RequiredArgsConstructor
public class KhamSangLocController {

    private final KhamSangLocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<KhamSangLocResponse>>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KhamSangLocResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<KhamSangLocResponse>> create(@RequestBody KhamSangLocRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<KhamSangLocResponse>> update(@PathVariable String id, @RequestBody KhamSangLocRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
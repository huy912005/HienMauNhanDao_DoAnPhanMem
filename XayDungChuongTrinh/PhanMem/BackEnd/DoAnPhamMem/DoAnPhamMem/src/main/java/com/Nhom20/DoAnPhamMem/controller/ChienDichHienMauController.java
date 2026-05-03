package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.service.ChienDichHienMauService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chiendich")
@RequiredArgsConstructor
public class ChienDichHienMauController {
    private final ChienDichHienMauService chienDichHienMauService;
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(){
        return new ResponseEntity<>(chienDichHienMauService.getAll(), HttpStatus.OK);
    }
}

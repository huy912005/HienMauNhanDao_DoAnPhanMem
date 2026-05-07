package com.Nhom20.DoAnPhamMem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/phieunhapxuat")
@lombok.RequiredArgsConstructor
@org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
public class PhieuNhapXuatController {
    private final com.Nhom20.DoAnPhamMem.service.PhieuNhapXuatService phieuNhapXuatService;

    // API chốt lưu phiếu nhập kho
    @org.springframework.web.bind.annotation.PostMapping("/import")
    public org.springframework.http.ResponseEntity<String> importBloodUnits(@org.springframework.web.bind.annotation.RequestBody com.Nhom20.DoAnPhamMem.dto.request.ImportBloodRequestDTO requestDTO) {
        phieuNhapXuatService.importBloodUnits(requestDTO);
        return org.springframework.http.ResponseEntity.ok("Nhập kho thành công.");
    }
}

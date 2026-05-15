package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.dto.request.KetQuaXetNghiemRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KetQuaXetNghiemResponse;
import com.Nhom20.DoAnPhamMem.service.KetQuaXetNghiemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ketquaxetnghiem")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KetQuaXetNghiemController {

    private final KetQuaXetNghiemService ketQuaXetNghiemService;
    @GetMapping
    public ResponseEntity<List<KetQuaXetNghiemResponse>> getAll() {
        return ResponseEntity.ok(ketQuaXetNghiemService.getAll());
    }
    @PostMapping
    public ResponseEntity<KetQuaXetNghiemResponse> create(@RequestBody KetQuaXetNghiemRequest request) {
        return ResponseEntity.ok(ketQuaXetNghiemService.create(request));
    }
    @GetMapping("/tui-mau/{maTuiMau}")
    public ResponseEntity<KetQuaXetNghiemResponse> getByMaTuiMau(@PathVariable String maTuiMau) {
        Optional<KetQuaXetNghiemResponse> result = ketQuaXetNghiemService.getByMaTuiMau(maTuiMau);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{maKQ}")
    public ResponseEntity<KetQuaXetNghiemResponse> update(
            @PathVariable String maKQ,
            @RequestBody KetQuaXetNghiemRequest request) {
        return ResponseEntity.ok(ketQuaXetNghiemService.update(maKQ, request));
    }
}

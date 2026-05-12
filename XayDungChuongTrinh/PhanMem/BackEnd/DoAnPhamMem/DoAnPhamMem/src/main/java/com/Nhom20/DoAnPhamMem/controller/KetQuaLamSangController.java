package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.dto.request.KetQuaLamSangRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KetQuaLamSangResponse;
import com.Nhom20.DoAnPhamMem.dto.response.ScreeningStatsResponse;
import com.Nhom20.DoAnPhamMem.service.KetQuaLamSangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ketqualamsang")
@RequiredArgsConstructor
public class KetQuaLamSangController {

    private final KetQuaLamSangService ketQuaLamSangService;

    @GetMapping
    public ResponseEntity<List<KetQuaLamSangResponse>> getAll() {
        return ResponseEntity.ok(ketQuaLamSangService.getAll());
    }

    @GetMapping("/waiting")
    public ResponseEntity<List<KetQuaLamSangResponse>> getWaiting() {
        return ResponseEntity.ok(ketQuaLamSangService.getWaiting());
    }

    @GetMapping("/stats")
    public ResponseEntity<ScreeningStatsResponse> getStats() {
        return ResponseEntity.ok(ketQuaLamSangService.getStats());
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody KetQuaLamSangRequest request) {
        ketQuaLamSangService.save(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{maKQ}")
    public ResponseEntity<Void> update(@PathVariable String maKQ, @RequestBody KetQuaLamSangRequest request) {
        ketQuaLamSangService.update(maKQ, request);
        return ResponseEntity.ok().build();
    }

    /** Cập nhật kết quả (POST — tránh môi trường chặn PUT / preflight) */
    @PostMapping("/{maKQ}/cap-nhat")
    public ResponseEntity<Void> capNhat(@PathVariable String maKQ, @RequestBody KetQuaLamSangRequest request) {
        ketQuaLamSangService.update(maKQ, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{maKQ}")
    public ResponseEntity<Void> delete(@PathVariable String maKQ) {
        ketQuaLamSangService.delete(maKQ);
        return ResponseEntity.noContent().build();
    }
}

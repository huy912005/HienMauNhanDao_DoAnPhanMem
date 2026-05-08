package com.Nhom20.DoAnPhamMem.controller;

import com.Nhom20.DoAnPhamMem.dto.response.CollectionStatsResponse;
import com.Nhom20.DoAnPhamMem.dto.response.TuiMauResponse;
import com.Nhom20.DoAnPhamMem.service.TuiMauService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tuimau")
@RequiredArgsConstructor
public class TuiMauController {

    private final TuiMauService tuiMauService;

    @GetMapping
    public ResponseEntity<List<TuiMauResponse>> getAll() {
        return ResponseEntity.ok(tuiMauService.getAll());
    }

    @GetMapping("/stats")
    public ResponseEntity<CollectionStatsResponse> getStats() {
        return ResponseEntity.ok(tuiMauService.getStats());
    }

    @DeleteMapping("/{maTuiMau}")
    public ResponseEntity<Void> delete(@PathVariable String maTuiMau) {
        tuiMauService.delete(maTuiMau);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{maTuiMau}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable String maTuiMau, @RequestParam String status) {
        tuiMauService.updateStatus(maTuiMau, status);
        return ResponseEntity.ok().build();
    }
}

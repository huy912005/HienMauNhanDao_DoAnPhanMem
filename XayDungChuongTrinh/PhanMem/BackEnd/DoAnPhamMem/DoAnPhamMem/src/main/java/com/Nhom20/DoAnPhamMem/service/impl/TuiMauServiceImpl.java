package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.TuiMauRequest;
import com.Nhom20.DoAnPhamMem.dto.response.TuiMauResponse;
import com.Nhom20.DoAnPhamMem.entity.TuiMauEntity;
import com.Nhom20.DoAnPhamMem.mapper.TuiMauMapper;
import com.Nhom20.DoAnPhamMem.repository.TuiMauRepository;
import com.Nhom20.DoAnPhamMem.service.TuiMauService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TuiMauServiceImpl implements TuiMauService {

    private final TuiMauRepository repository;
    private final TuiMauMapper mapper;

    @Override
    public ApiResponse<List<TuiMauResponse>> getAll() {
        List<TuiMauResponse> responses = repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<TuiMauResponse>>builder()
                .status(true)
                .message("Lấy danh sách túi máu thành công")
                .data(responses)
                .build();
    }

    @Override
    public ApiResponse<TuiMauResponse> getById(String id) {
        TuiMauEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy túi máu với mã: " + id));
        return ApiResponse.<TuiMauResponse>builder()
                .status(true)
                .message("Lấy thông tin túi máu thành công")
                .data(mapper.toResponse(entity))
                .build();
    }

    @Override
    public ApiResponse<TuiMauResponse> create(TuiMauRequest request) {
        TuiMauEntity entity = mapper.toEntity(request);
        
        if (entity.getMaTuiMau() == null || entity.getMaTuiMau().isEmpty()) {
            // Tự động sinh mã túi máu (7 ký tự)
            entity.setMaTuiMau("TM" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        }
        if (entity.getThoiGianLayMau() == null) {
            entity.setThoiGianLayMau(LocalDateTime.now());
        }
        
        TuiMauEntity saved = repository.save(entity);
        return ApiResponse.<TuiMauResponse>builder()
                .status(true)
                .message("Tạo túi máu thành công")
                .data(mapper.toResponse(saved))
                .build();
    }

    @Override
    public ApiResponse<TuiMauResponse> update(String id, TuiMauRequest request) {
        TuiMauEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy túi máu với mã: " + id));

        existing.setMaKho(request.getMaKho());
        existing.setTheTich(request.getTheTich());
        existing.setThoiGianLayMau(request.getThoiGianLayMau());
        existing.setTrangThai(request.getTrangThai());
        existing.setNhietDoVanChuyen(request.getNhietDoVanChuyen());

        TuiMauEntity updated = repository.save(existing);
        return ApiResponse.<TuiMauResponse>builder()
                .status(true)
                .message("Cập nhật túi máu thành công")
                .data(mapper.toResponse(updated))
                .build();
    }

    @Override
    public ApiResponse<Void> delete(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy túi máu với mã: " + id);
        }
        repository.deleteById(id);
        return ApiResponse.<Void>builder()
                .status(true)
                .message("Xóa túi máu thành công")
                .build();
    }
}
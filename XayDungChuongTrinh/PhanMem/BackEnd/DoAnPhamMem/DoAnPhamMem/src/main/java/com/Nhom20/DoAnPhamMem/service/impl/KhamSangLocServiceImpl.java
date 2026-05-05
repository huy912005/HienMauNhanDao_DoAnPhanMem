package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.KhamSangLocRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KhamSangLocResponse;
import com.Nhom20.DoAnPhamMem.entity.KetQuaLamSangEntity;
import com.Nhom20.DoAnPhamMem.mapper.KhamSangLocMapper;
import com.Nhom20.DoAnPhamMem.repository.KetQuaLamSangRepository;
import com.Nhom20.DoAnPhamMem.service.KhamSangLocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KhamSangLocServiceImpl implements KhamSangLocService {

    private final KetQuaLamSangRepository repository;
    private final KhamSangLocMapper mapper;

    @Override
    public ApiResponse<List<KhamSangLocResponse>> getAll() {
        List<KhamSangLocResponse> responses = repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<KhamSangLocResponse>>builder()
                .status(true)
                .message("Lấy danh sách khám sàng lọc thành công")
                .data(responses)
                .build();
    }

    @Override
    public ApiResponse<KhamSangLocResponse> getById(String id) {
        KetQuaLamSangEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu khám sàng lọc với mã: " + id));
        return ApiResponse.<KhamSangLocResponse>builder()
                .status(true)
                .message("Lấy thông tin khám sàng lọc thành công")
                .data(mapper.toResponse(entity))
                .build();
    }

    @Override
    public ApiResponse<KhamSangLocResponse> create(KhamSangLocRequest request) {
        KetQuaLamSangEntity entity = mapper.toEntity(request);
        
        if (entity.getMaKQ() == null || entity.getMaKQ().isEmpty()) {
            // Tự động sinh mã khám lâm sàng (7 ký tự) an toàn nếu FE không truyền xuống
            entity.setMaKQ("KL" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        }
        
        KetQuaLamSangEntity saved = repository.save(entity);
        return ApiResponse.<KhamSangLocResponse>builder()
                .status(true)
                .message("Tạo phiếu khám sàng lọc thành công")
                .data(mapper.toResponse(saved))
                .build();
    }

    @Override
    public ApiResponse<KhamSangLocResponse> update(String id, KhamSangLocRequest request) {
        KetQuaLamSangEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu khám sàng lọc với mã: " + id));

        existing.setHuyetAp(request.getHuyetAp());
        existing.setNhipTim(request.getNhipTim());
        existing.setCanNang(request.getCanNang());
        existing.setNhietDo(request.getNhietDo());
        existing.setKetQua(request.getKetQua());
        existing.setLyDoTuChoi(request.getLyDoTuChoi());

        KetQuaLamSangEntity updated = repository.save(existing);
        return ApiResponse.<KhamSangLocResponse>builder()
                .status(true)
                .message("Cập nhật phiếu khám sàng lọc thành công")
                .data(mapper.toResponse(updated))
                .build();
    }

    @Override
    public ApiResponse<Void> delete(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy phiếu khám sàng lọc với mã: " + id);
        }
        repository.deleteById(id);
        return ApiResponse.<Void>builder()
                .status(true)
                .message("Xóa phiếu khám sàng lọc thành công")
                .build();
    }
}
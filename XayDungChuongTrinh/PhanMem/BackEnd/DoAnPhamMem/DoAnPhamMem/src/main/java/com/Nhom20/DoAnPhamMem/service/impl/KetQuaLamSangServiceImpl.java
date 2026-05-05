package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.KetQuaLamSangRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KetQuaLamSangResponse;
import com.Nhom20.DoAnPhamMem.entity.KetQuaLamSangEntity;
import com.Nhom20.DoAnPhamMem.mapper.KetQuaLamSangMapper;
import com.Nhom20.DoAnPhamMem.repository.KetQuaLamSangRepository;
import com.Nhom20.DoAnPhamMem.service.KetQuaLamSangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KetQuaLamSangServiceImpl implements KetQuaLamSangService {

    @Autowired
    private KetQuaLamSangRepository repository;

    @Autowired
    private KetQuaLamSangMapper mapper;

    @Override
    public ApiResponse<List<KetQuaLamSangResponse>> getAll() {
        List<KetQuaLamSangResponse> responses = repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<KetQuaLamSangResponse>>builder()
                .status(true)
                .message("Lấy danh sách thành công")
                .data(responses)
                .build();
    }

    @Override
    public ApiResponse<KetQuaLamSangResponse> getById(String id) {
        KetQuaLamSangEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kết quả khám với mã: " + id));
        return ApiResponse.<KetQuaLamSangResponse>builder()
                .status(true)
                .message("Lấy thông tin thành công")
                .data(mapper.toResponse(entity))
                .build();
    }
}

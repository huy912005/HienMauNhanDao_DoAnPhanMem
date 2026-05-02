package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.ChiendichHienMauResponse;
import com.Nhom20.DoAnPhamMem.entity.ChienDichHienMauEntity;
import com.Nhom20.DoAnPhamMem.mapper.ChiendichHienMauMapper;
import com.Nhom20.DoAnPhamMem.repository.ChienDichHienMauRepository;
import com.Nhom20.DoAnPhamMem.service.ChienDichHienMauService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChienDichHienMauServiceImpl implements ChienDichHienMauService {
    private final ChienDichHienMauRepository chienDichHienMauRepository;
    private final ChiendichHienMauMapper mapper;
    @Override
    public ApiResponse<List<ChiendichHienMauResponse>> getAll() {
        List<ChienDichHienMauEntity> listChienDich = chienDichHienMauRepository.findAll();
        List<ChiendichHienMauResponse> responseList = mapper.toResponseList(listChienDich);
        return ApiResponse.<List<ChiendichHienMauResponse>>builder().status(true).message("Lấy danh sách chiến dịch thành công").data(responseList).build();
    }
}

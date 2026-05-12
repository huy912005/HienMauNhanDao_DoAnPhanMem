package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.DiaDiemResponse;
import com.Nhom20.DoAnPhamMem.entity.DiaDiemEntity;
import com.Nhom20.DoAnPhamMem.mapper.DiaDiemMapper;
import com.Nhom20.DoAnPhamMem.repository.DiaDiemRepository;
import com.Nhom20.DoAnPhamMem.service.DiaDiemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DiaDiemServiceImpl implements DiaDiemService {
    private final DiaDiemRepository diaDiemRepository;
    private final DiaDiemMapper diaDiemMapper;
    @Override
    public ApiResponse<List<DiaDiemResponse>> getAll() {
        List<DiaDiemEntity> diaDiemEntityList = diaDiemRepository.findAll();
        return new ApiResponse<>("Lấy thành công danh sách địa điểm",true, diaDiemMapper.toResponseList(diaDiemEntityList));
    }
}

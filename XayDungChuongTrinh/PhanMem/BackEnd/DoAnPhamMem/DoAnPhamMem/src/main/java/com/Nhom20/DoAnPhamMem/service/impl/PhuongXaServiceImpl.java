package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.PhuongXaResponse;
import com.Nhom20.DoAnPhamMem.mapper.PhuongXaMapper;
import com.Nhom20.DoAnPhamMem.repository.PhuongXaRepository;
import com.Nhom20.DoAnPhamMem.service.PhuongXaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhuongXaServiceImpl implements PhuongXaService {
    private final PhuongXaRepository phuongXaRepository;
    private final PhuongXaMapper phuongXaMapper;
    @Override
    public ApiResponse<List<PhuongXaResponse>> getAll() {
        List<PhuongXaResponse> phuongXaResponses = phuongXaMapper.toResponseList(phuongXaRepository.findAll());
        return ApiResponse.<List<PhuongXaResponse>>builder().status(true).message("Lấy tất cả phường xã thành công!").data(phuongXaResponses).build();
    }
}

package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.HoSoSucKhoeResponse;
import com.Nhom20.DoAnPhamMem.mapper.HoSoSucKhoeMapper;
import com.Nhom20.DoAnPhamMem.repository.HoSoSucKhoeRepository;
import com.Nhom20.DoAnPhamMem.service.HoSoSucKhoeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoSoSucKhoeServiceImpl implements HoSoSucKhoeService {
    private final HoSoSucKhoeRepository hoSoSucKhoeRepository;
    private  final HoSoSucKhoeMapper hoSoSucKhoeMapper;
    @Override
    public ApiResponse<List<HoSoSucKhoeResponse>> getAll() {
        List<HoSoSucKhoeResponse> listHoSoChienDich = hoSoSucKhoeMapper.toResponseList(hoSoSucKhoeRepository.findAll());
        return ApiResponse.<List<HoSoSucKhoeResponse>>builder().data(listHoSoChienDich).message("Lấy danh sách thành công!").status(true).build();
    }
}

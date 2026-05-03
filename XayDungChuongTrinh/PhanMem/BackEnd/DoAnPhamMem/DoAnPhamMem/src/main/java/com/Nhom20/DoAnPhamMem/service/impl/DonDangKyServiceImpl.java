package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.DonDangKyRequest;
import com.Nhom20.DoAnPhamMem.dto.response.DonDangKyResponse;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;
import com.Nhom20.DoAnPhamMem.mapper.DonDangKyMapper;
import com.Nhom20.DoAnPhamMem.repository.DonDangKyRepository;
import com.Nhom20.DoAnPhamMem.service.DonDangKyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DonDangKyServiceImpl implements DonDangKyService {
    private final DonDangKyRepository repository;
    private final DonDangKyMapper mapper;
    @Override
    public ApiResponse<DonDangKyResponse> createDonDangKy(DonDangKyRequest request) {
        DonDangKyEntity donDangKyEntity = mapper.toEntity(request);
        donDangKyEntity.setThoiGianDangKy(LocalDateTime.now());
        donDangKyEntity.setTrangThai(TrangThaiDonDangKy.DA_DANG_KY);
        DonDangKyEntity savedDonDangKyEntity = repository.save(donDangKyEntity);
        DonDangKyResponse responseData = mapper.toResponse(savedDonDangKyEntity);
        return ApiResponse.<DonDangKyResponse>builder().message("Đăng ký thành công!").status(true).data(responseData).build() ;
    }
}

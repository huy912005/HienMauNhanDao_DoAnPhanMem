package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.ChiendichHienMauResponse;
import com.Nhom20.DoAnPhamMem.entity.ChienDichHienMauEntity;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;
import com.Nhom20.DoAnPhamMem.mapper.ChiendichHienMauMapper;
import com.Nhom20.DoAnPhamMem.repository.ChienDichHienMauRepository;
import com.Nhom20.DoAnPhamMem.repository.DonDangKyRepository;
import com.Nhom20.DoAnPhamMem.service.ChienDichHienMauService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChienDichHienMauServiceImpl implements ChienDichHienMauService {
    private final ChienDichHienMauRepository chienDichHienMauRepository;
    private final DonDangKyRepository donDangKyRepository;
    private final com.Nhom20.DoAnPhamMem.repository.TuiMauRepository tuiMauRepository;
    private final ChiendichHienMauMapper mapper;

    @Override
    public ApiResponse<List<ChiendichHienMauResponse>> getAll() {
        List<ChienDichHienMauEntity> campaigns = chienDichHienMauRepository.findAll();
        
        // Đếm số túi máu THỰC TẾ đã NHẬP KHO theo từng chiến dịch
        List<com.Nhom20.DoAnPhamMem.entity.TuiMauEntity> allTuiMau = tuiMauRepository.findAll();
        Map<String, Long> countMap = allTuiMau.stream()
                .filter(tm -> tm.getTrangThai() == com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau.NHAP_KHO 
                           && tm.getDonDangKy() != null 
                           && tm.getDonDangKy().getChienDich() != null)
                .collect(Collectors.groupingBy(
                        tm -> tm.getDonDangKy().getChienDich().getMaChienDich(),
                        Collectors.counting()
                ));
        List<ChiendichHienMauResponse> responseList = campaigns.stream().map(cd -> {
            ChiendichHienMauResponse res = mapper.toResponse(cd);
            Long count = countMap.getOrDefault(cd.getMaChienDich(), 0L);
            res.setLuongMauDaThu(count.intValue());
            return res;
        }).collect(Collectors.toList());

        return ApiResponse.<List<ChiendichHienMauResponse>>builder().status(true).data(responseList).build();
    }
}

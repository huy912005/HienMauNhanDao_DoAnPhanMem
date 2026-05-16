package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.service.KhoMauService;
import org.springframework.stereotype.Service;

@Service
@lombok.RequiredArgsConstructor
public class KhoMauServiceImpl implements KhoMauService {
    private final com.Nhom20.DoAnPhamMem.repository.KhoMauRepository khoMauRepository;

    @Override
    public java.util.List<com.Nhom20.DoAnPhamMem.dto.response.BloodTypeStatDTO> getInventoryByBloodType() {
        return khoMauRepository.getInventoryByBloodType();
    }

    @Override
    public java.util.List<com.Nhom20.DoAnPhamMem.dto.response.KhoMauResponse> getAllKhoMau() {
        return khoMauRepository.findAll().stream().map(kho -> com.Nhom20.DoAnPhamMem.dto.response.KhoMauResponse.builder()
                .maKho(kho.getMaKho())
                .nhomMau(kho.getNhomMau() != null ? kho.getNhomMau().getDbValue() : null)
                .soLuongTon(kho.getSoLuongTon())
                .nguongAnToan(kho.getNguongAnToan())
                .build()).collect(java.util.stream.Collectors.toList());
    }
}

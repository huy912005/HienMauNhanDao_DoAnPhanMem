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
}

package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.DonDangKyRequest;
import com.Nhom20.DoAnPhamMem.dto.response.DonDangKyResponse;
import com.Nhom20.DoAnPhamMem.entity.ChienDichHienMauEntity;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.entity.TinhNguyenVienEntity;
import com.Nhom20.DoAnPhamMem.enums.TheTich;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;
import com.Nhom20.DoAnPhamMem.mapper.DonDangKyMapper;
import com.Nhom20.DoAnPhamMem.repository.ChienDichHienMauRepository;
import com.Nhom20.DoAnPhamMem.repository.DonDangKyRepository;
import com.Nhom20.DoAnPhamMem.repository.TinhNguyenVienRepository;
import com.Nhom20.DoAnPhamMem.service.DonDangKyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DonDangKyServiceImpl implements DonDangKyService {

    private final DonDangKyRepository repository;
    private final DonDangKyMapper mapper;
    private final TinhNguyenVienRepository tinhNguyenVienRepository;
    private final ChienDichHienMauRepository chienDichRepository;

    @Override
    public ApiResponse<DonDangKyResponse> createDonDangKy(DonDangKyRequest request) {
        boolean daDangKy = repository.findByTinhNguyenVien_MaTNVAndChienDich_MaChienDich(request.getMaTNV(), request.getMaChienDich()).isPresent();
        if (daDangKy)
            return ApiResponse.<DonDangKyResponse>builder().status(false).message("Bạn đã đăng ký chiến dịch này rồi!").data(null).build();
        DonDangKyEntity entity = mapper.toEntity(request);
        Integer max = repository.findMaxMaDon();
        entity.setMaDon(String.format("DK%05d", (max == null) ? 1 : max + 1));
        // Set maQR tự sinh
        entity.setMaQR("QR_" + entity.getMaDon());
        entity.setThoiGianDangKy(LocalDateTime.now());
        entity.setTrangThai(TrangThaiDonDangKy.DA_DANG_KY);
        if (request.getTheTich() != null && request.getTheTich() > 0) {
            entity.setTheTich(TheTich.fromDbValue(request.getTheTich()));
        }
        TinhNguyenVienEntity tnv = tinhNguyenVienRepository.findById(request.getMaTNV()).orElseThrow(() -> new RuntimeException("Không tìm thấy tình nguyện viên: " + request.getMaTNV()));
        entity.setTinhNguyenVien(tnv);
        ChienDichHienMauEntity chienDich = chienDichRepository.findById(request.getMaChienDich()).orElseThrow(() -> new RuntimeException("Không tìm thấy chiến dịch: " + request.getMaChienDich()));
        entity.setChienDich(chienDich);
        DonDangKyEntity saved = repository.save(entity);
        return ApiResponse.<DonDangKyResponse>builder().message("Đăng ký thành công!").status(true).data(mapper.toResponse(saved)).build();
    }

    @Override
    public ApiResponse<DonDangKyResponse> checkDaDangKy(String maTNV, String maChienDich) {
        return repository.findByTinhNguyenVien_MaTNVAndChienDich_MaChienDich(maTNV, maChienDich)
                .map(entity -> ApiResponse.<DonDangKyResponse>builder().status(true).message("Đã đăng ký").data(mapper.toResponse(entity)).build())
                .orElse(ApiResponse.<DonDangKyResponse>builder().status(false).message("Chưa đăng ký").data(null).build());
    }
}

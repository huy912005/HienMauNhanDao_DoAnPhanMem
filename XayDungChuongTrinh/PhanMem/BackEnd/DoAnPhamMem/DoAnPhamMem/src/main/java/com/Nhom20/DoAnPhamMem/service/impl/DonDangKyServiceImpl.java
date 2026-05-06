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
        // -----------------------------------------------------------------------
        // Kiểm tra trùng lặp: 1 TNV chỉ được đăng ký 1 lần cho 1 chiến dịch
        // -----------------------------------------------------------------------
        boolean daDangKy = repository
                .findByTinhNguyenVien_MaTNVAndChienDich_MaChienDich(
                        request.getMaTNV(), request.getMaChienDich())
                .isPresent();
        if (daDangKy) {
            return ApiResponse.<DonDangKyResponse>builder()
                    .status(false)
                    .message("Bạn đã đăng ký chiến dịch này rồi!")
                    .data(null)
                    .build();
        }

        // Mapper map các field thông thường từ request
        DonDangKyEntity entity = mapper.toEntity(request);

        // Set mã PK - format DK + 5 số = 7 ký tự (khớp CHAR(7))
        Integer max = repository.findMaxMaDon();
        entity.setMaDon(String.format("DK%05d", (max == null) ? 1 : max + 1));

        // Set maQR tự sinh
        entity.setMaQR("QR_" + entity.getMaDon());

        // Set thời gian đăng ký (trường thời gian - service set)
        entity.setThoiGianDangKy(LocalDateTime.now());

        // Set enum trạng thái mặc định
        entity.setTrangThai(TrangThaiDonDangKy.DA_DANG_KY);

        // Set enum thể tích (nếu có, mặc định null ~ 0 khi "Chưa hiến")
        if (request.getTheTich() != null && request.getTheTich() > 0) {
            entity.setTheTich(TheTich.fromDbValue(request.getTheTich()));
        }

        // Set khóa ngoại entity: tinhNguyenVien và chienDich
        TinhNguyenVienEntity tnv = tinhNguyenVienRepository.findById(request.getMaTNV())
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy tình nguyện viên: " + request.getMaTNV()));
        entity.setTinhNguyenVien(tnv);

        ChienDichHienMauEntity chienDich = chienDichRepository.findById(request.getMaChienDich())
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy chiến dịch: " + request.getMaChienDich()));
        entity.setChienDich(chienDich);

        DonDangKyEntity saved = repository.save(entity);

        return ApiResponse.<DonDangKyResponse>builder()
                .message("Đăng ký thành công!")
                .status(true)
                .data(mapper.toResponse(saved))
                .build();
    }

    @Override
    public ApiResponse<DonDangKyResponse> checkDaDangKy(String maTNV, String maChienDich) {
        return repository.findByTinhNguyenVien_MaTNVAndChienDich_MaChienDich(maTNV, maChienDich)
                .map(entity -> ApiResponse.<DonDangKyResponse>builder()
                        .status(true)
                        .message("Đã đăng ký")
                        .data(mapper.toResponse(entity))
                        .build())
                .orElse(ApiResponse.<DonDangKyResponse>builder()
                        .status(false)
                        .message("Chưa đăng ký")
                        .data(null)
                        .build());
    }
}

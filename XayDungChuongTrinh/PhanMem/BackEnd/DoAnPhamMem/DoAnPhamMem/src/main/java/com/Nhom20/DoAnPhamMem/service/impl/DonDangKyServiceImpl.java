package com.Nhom20.DoAnPhamMem.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.DonDangKyRequest;
import com.Nhom20.DoAnPhamMem.dto.response.DonDangKyResponse;
import com.Nhom20.DoAnPhamMem.entity.ChienDichHienMauEntity;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.entity.KhoMauEntity;
import com.Nhom20.DoAnPhamMem.entity.NhanVienEntity;
import com.Nhom20.DoAnPhamMem.entity.TinhNguyenVienEntity;
import com.Nhom20.DoAnPhamMem.entity.TuiMauEntity;
import com.Nhom20.DoAnPhamMem.enums.TheTich;
import com.Nhom20.DoAnPhamMem.enums.TheTichTuiMau;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau;
import com.Nhom20.DoAnPhamMem.mapper.DonDangKyMapper;
import com.Nhom20.DoAnPhamMem.repository.ChienDichHienMauRepository;
import com.Nhom20.DoAnPhamMem.repository.DonDangKyRepository;
import com.Nhom20.DoAnPhamMem.repository.TinhNguyenVienRepository;
import com.Nhom20.DoAnPhamMem.service.DonDangKyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DonDangKyServiceImpl implements DonDangKyService {

        private final DonDangKyRepository repository;
        private final DonDangKyMapper mapper;
        private final TinhNguyenVienRepository tinhNguyenVienRepository;
        private final ChienDichHienMauRepository chienDichRepository;
        private final com.Nhom20.DoAnPhamMem.repository.NhanVienRepository nhanVienRepository;
        private final com.Nhom20.DoAnPhamMem.repository.TuiMauRepository tuiMauRepository;
        private final com.Nhom20.DoAnPhamMem.repository.KhoMauRepository khoMauRepository;

        @Override
        public ApiResponse<DonDangKyResponse> createDonDangKy(DonDangKyRequest request) {
                boolean daDangKy = repository
                                .findByTinhNguyenVien_MaTNVAndChienDich_MaChienDich(request.getMaTNV(),
                                                request.getMaChienDich())
                                .isPresent();
                if (daDangKy)
                        return ApiResponse.<DonDangKyResponse>builder().status(false)
                                        .message("Bạn đã đăng ký chiến dịch này rồi!")
                                        .data(null).build();
                DonDangKyEntity entity = mapper.toEntity(request);
                Integer max = repository.findMaxMaDon();
                entity.setMaDon(String.format("DK%05d", (max == null) ? 1 : max + 1));
                entity.setMaQR("QR_" + entity.getMaDon());
                entity.setThoiGianDangKy(LocalDateTime.now());
                entity.setTrangThai(TrangThaiDonDangKy.DA_DANG_KY);
                if (request.getTheTich() != null && request.getTheTich() > 0) {
                        entity.setTheTich(TheTich.fromDbValue(request.getTheTich()));
                }
                TinhNguyenVienEntity tnv = tinhNguyenVienRepository.findById(request.getMaTNV())
                                .orElseThrow(() -> new RuntimeException(
                                                "Không tìm thấy tình nguyện viên: " + request.getMaTNV()));
                entity.setTinhNguyenVien(tnv);
                ChienDichHienMauEntity chienDich = chienDichRepository.findById(request.getMaChienDich())
                                .orElseThrow(() -> new RuntimeException(
                                                "Không tìm thấy chiến dịch: " + request.getMaChienDich()));
                entity.setChienDich(chienDich);

                // Lấy nhân viên từ email gửi lên từ frontend (localStorage.getItem('email'))
                String email = request.getEmailNhanVien();
                if (email != null && !email.isBlank()) {
                        nhanVienRepository.findByTaiKhoan_Email(email)
                                        .ifPresent(entity::setNhanVienPhuTrach);
                }

                DonDangKyEntity saved = repository.save(entity);
                return ApiResponse.<DonDangKyResponse>builder().message("Đăng ký thành công!").status(true)
                                .data(mapper.toResponse(saved)).build();
        }

        @Override
        public ApiResponse<DonDangKyResponse> checkDaDangKy(String maTNV, String maChienDich) {
                return repository.findByTinhNguyenVien_MaTNVAndChienDich_MaChienDich(maTNV, maChienDich)
                                .map(entity -> ApiResponse.<DonDangKyResponse>builder().status(true)
                                                .message("Đã đăng ký")
                                                .data(mapper.toResponse(entity)).build())
                                .orElse(ApiResponse.<DonDangKyResponse>builder().status(false).message("Chưa đăng ký")
                                                .data(null)
                                                .build());
        }

        @Override
        public ApiResponse<org.springframework.data.domain.Page<DonDangKyResponse>> getAll(
                        org.springframework.data.domain.Pageable pageable) {
                org.springframework.data.domain.Page<DonDangKyEntity> page = repository.findAll(pageable);
                org.springframework.data.domain.Page<DonDangKyResponse> responsePage = page.map(mapper::toResponse);
                return ApiResponse.<org.springframework.data.domain.Page<DonDangKyResponse>>builder()
                                .status(true)
                                .message("Lấy danh sách đơn đăng ký thành công")
                                .data(responsePage)
                                .build();
        }

        @Override
        public ApiResponse<DonDangKyResponse> updateDonDangKy(String maDon, DonDangKyRequest request) {
                DonDangKyEntity entity = repository.findById(maDon)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đăng ký: " + maDon));
                if (request.getTheTich() != null) {
                        entity.setTheTich(TheTich.fromDbValue(request.getTheTich()));
                }
                if (request.getMaChienDich() != null) {
                        ChienDichHienMauEntity chienDich = chienDichRepository.findById(request.getMaChienDich())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Không tìm thấy chiến dịch: " + request.getMaChienDich()));
                        entity.setChienDich(chienDich);
                }
                DonDangKyEntity saved = repository.save(entity);
                return ApiResponse.<DonDangKyResponse>builder().status(true).message("Cập nhật thành công")
                                .data(mapper.toResponse(saved)).build();
        }

        @Override
        @org.springframework.transaction.annotation.Transactional
        public ApiResponse<DonDangKyResponse> cancelDonDangKy(String maDon, String maNhanVien) {
                DonDangKyEntity entity = repository.findById(maDon)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đăng ký: " + maDon));

                // 1. Cập nhật trạng thái đơn thành 'Chưa hiến' (DA_HUY enum đã map tới 'Chưa
                // hiến')
                entity.setTrangThai(TrangThaiDonDangKy.DA_HUY);
                entity.setTheTich(TheTich.ML_0);
                repository.save(entity);

                // 2. Tạo túi máu với trạng thái 'Hủy' nếu có mã nhân viên (do NVYT thực hiện
                // hủy)
                if (maNhanVien != null && !maNhanVien.isBlank()) {
                        NhanVienEntity nv = nhanVienRepository.findById(maNhanVien)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Không tìm thấy nhân viên: " + maNhanVien));

                        TinhNguyenVienEntity tnv = entity.getTinhNguyenVien();
                        KhoMauEntity kho = null;
                        if (tnv.getNhomMau() != null) {
                                kho = khoMauRepository.findByNhomMau(tnv.getNhomMau())
                                                .orElse(null);
                        }

                        Integer maxId = tuiMauRepository.findMaxMaTuiMau();
                        String nextMaTuiMau = "TM" + String.format("%05d", (maxId != null ? maxId + 1 : 1));

                        TuiMauEntity tuiMau = new TuiMauEntity();
                        tuiMau.setMaTuiMau(nextMaTuiMau);
                        tuiMau.setDonDangKy(entity);
                        tuiMau.setNhanVien(nv);
                        tuiMau.setKhoMau(kho);
                        tuiMau.setTheTich(TheTichTuiMau.ML_250); // Mặc định cho túi hủy
                        tuiMau.setThoiGianLayMau(LocalDateTime.now());
                        tuiMau.setTrangThai(TrangThaiTuiMau.HUY);
                        tuiMau.setNhietDoVanChuyen(0.0);

                        tuiMauRepository.save(tuiMau);
                }

                return ApiResponse.<DonDangKyResponse>builder().status(true).message("Hủy đăng ký thành công")
                                .data(mapper.toResponse(entity)).build();
        }

        @Override
        public ApiResponse<org.springframework.data.domain.Page<DonDangKyResponse>> getByMaTNV(String maTNV,
                        org.springframework.data.domain.Pageable pageable) {
                org.springframework.data.domain.Page<DonDangKyEntity> page = repository.findByTinhNguyenVien_MaTNV(
                                maTNV,
                                pageable);
                org.springframework.data.domain.Page<DonDangKyResponse> responsePage = page.map(mapper::toResponse);
                return ApiResponse.<org.springframework.data.domain.Page<DonDangKyResponse>>builder()
                                .status(true)
                                .message("Lấy danh sách đơn đăng ký thành công")
                                .data(responsePage)
                                .build();
        }

        @Override
        public ApiResponse<DonDangKyResponse> getById(String maDon) {
                DonDangKyEntity entity = repository.findById(maDon)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đăng ký: " + maDon));
                return ApiResponse.<DonDangKyResponse>builder()
                                .status(true)
                                .message("Lấy thông tin đơn đăng ký thành công")
                                .data(mapper.toResponse(entity))
                                .build();
        }

        @Override
        public ApiResponse<Void> deleteDonDangKy(String maDon) {
                if (!repository.existsById(maDon))
                        throw new RuntimeException("Không tìm thấy đơn đăng ký: " + maDon);
                repository.deleteById(maDon);
                return ApiResponse.<Void>builder().status(true).message("Xóa thành công").build();
        }

        @Override
        public ApiResponse<org.springframework.data.domain.Page<DonDangKyResponse>> getReadyForCollection(
                        org.springframework.data.domain.Pageable pageable) {
                org.springframework.data.domain.Page<DonDangKyEntity> page = repository
                                .findReadyForCollection(pageable);
                org.springframework.data.domain.Page<DonDangKyResponse> responsePage = page.map(mapper::toResponse);
                return ApiResponse.<org.springframework.data.domain.Page<DonDangKyResponse>>builder()
                                .status(true)
                                .message("Lấy danh sách chờ thu nhận thành công")
                                .data(responsePage)
                                .build();
        }
}

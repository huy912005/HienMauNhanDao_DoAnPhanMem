package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.service.PhieuNhapXuatService;
import org.springframework.stereotype.Service;

@Service
@lombok.RequiredArgsConstructor
public class PhieuNhapXuatServiceImpl implements PhieuNhapXuatService {

    private final com.Nhom20.DoAnPhamMem.repository.NhanVienRepository nhanVienRepository;
    private final com.Nhom20.DoAnPhamMem.repository.PhieuNhapXuatRepository phieuNhapXuatRepository;
    private final com.Nhom20.DoAnPhamMem.repository.TuiMauRepository tuiMauRepository;
    private final com.Nhom20.DoAnPhamMem.repository.ChiTietNhapXuatRepository chiTietNhapXuatRepository;
    private final com.Nhom20.DoAnPhamMem.repository.KhoMauRepository khoMauRepository;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void importBloodUnits(com.Nhom20.DoAnPhamMem.dto.request.ImportBloodRequestDTO requestDTO) {
        // 1. Tìm nhân viên thực hiện
        com.Nhom20.DoAnPhamMem.entity.NhanVienEntity nhanVien = nhanVienRepository.findById(requestDTO.getMaNhanVien())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên thực hiện."));

        // 2. Tạo Phiếu Nhập Xuất mới
        com.Nhom20.DoAnPhamMem.entity.PhieuNhapXuatEntity phieu = new com.Nhom20.DoAnPhamMem.entity.PhieuNhapXuatEntity();
        String maPhieu = "PN" + System.currentTimeMillis() % 100000;
        phieu.setMaPhieu(maPhieu);
        phieu.setNhanVienThucHien(nhanVien);
        phieu.setLoaiPhieu(com.Nhom20.DoAnPhamMem.enums.LoaiPhieuNhapXuat.NHAP_KHO);
        phieu.setNgayNhapXuat(java.time.LocalDate.now());
        phieuNhapXuatRepository.save(phieu);

        // 3. Xử lý từng túi máu được quét
        for (String maTuiMau : requestDTO.getMaTuiMauList()) {
            com.Nhom20.DoAnPhamMem.entity.TuiMauEntity tuiMau = tuiMauRepository.findById(maTuiMau)
                    .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy túi máu " + maTuiMau));

            com.Nhom20.DoAnPhamMem.entity.ChiTietNhapXuatEntity chiTiet = new com.Nhom20.DoAnPhamMem.entity.ChiTietNhapXuatEntity();
            chiTiet.setPhieuNhapXuat(phieu);
            chiTiet.setTuiMau(tuiMau);
            chiTietNhapXuatRepository.save(chiTiet);

            tuiMau.setTrangThai(com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau.NHAP_KHO);
            tuiMauRepository.save(tuiMau);

            com.Nhom20.DoAnPhamMem.entity.KhoMauEntity khoMau = tuiMau.getKhoMau();
            if (khoMau != null) {
                khoMau.setSoLuongTon(khoMau.getSoLuongTon() + 1);
                khoMauRepository.save(khoMau);
            }
        }
    }
}

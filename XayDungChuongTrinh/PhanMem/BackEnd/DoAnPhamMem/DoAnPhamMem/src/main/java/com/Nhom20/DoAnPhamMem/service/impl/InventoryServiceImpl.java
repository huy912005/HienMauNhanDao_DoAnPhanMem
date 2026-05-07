package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.dto.request.ImportBloodRequestDTO;
import com.Nhom20.DoAnPhamMem.dto.response.*;
import com.Nhom20.DoAnPhamMem.repository.*;
import com.Nhom20.DoAnPhamMem.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final TuiMauRepository tuiMauRepository;
    private final KhoMauRepository khoMauRepository;
    private final TinhNguyenVienRepository tinhNguyenVienRepository;
    private final ChienDichHienMauRepository chienDichRepository;
    private final KetQuaLamSangRepository ketQuaLamSangRepository;
    private final PhieuNhapXuatRepository phieuNhapXuatRepository;
    private final ChiTietNhapXuatRepository chiTietNhapXuatRepository;
    private final NhanVienRepository nhanVienRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        return null;
    }

    @Override
    public List<BloodTypeStatDTO> getInventoryByBloodType() {
        return null;
    }

    @Override
    public List<MonthlyCollectionStatDTO> getBloodCollectionByMonth(int year) {
        return null;
    }

    @Override
    public Page<BloodUnitDTO> getBloodUnits(int page, int size, String search, String bloodType) {
        return null;
    }

    @Override
    public void deleteBloodUnit(String maTuiMau) {

    }

    @Override
    public BloodUnitDTO scanBloodUnit(String maTuiMau) {
        return null;
    }

    @Override
    public void importBloodUnits(ImportBloodRequestDTO requestDTO) {

    }

    @Override
    public Page<BloodUnitDTO> getBloodUnits(int page, int size, String search, String bloodType) {
        // Chuẩn bị phân trang
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);

        // Convert chuỗi bloodType (VD: "O+") sang Enum nếu người dùng có chọn bộ lọc
        com.Nhom20.DoAnPhamMem.enums.NhomMau enumNhomMau = null;
        if (bloodType != null && !bloodType.isEmpty()) {
            enumNhomMau = com.Nhom20.DoAnPhamMem.enums.NhomMau.fromDbValue(bloodType);
        }

        // Gọi hàm Query trong Repository
        Page<com.Nhom20.DoAnPhamMem.entity.TuiMauEntity> entityPage = tuiMauRepository.searchAndFilterBloodUnits(search, enumNhomMau, pageable);

        // Chuyển đổi Entity sang DTO để trả về cho Frontend
        return entityPage.map(tuiMau -> {
            BloodUnitDTO dto = new BloodUnitDTO();
            dto.setMaTuiMau(tuiMau.getMaTuiMau());
            dto.setNhomMau(tuiMau.getKhoMau() != null && tuiMau.getKhoMau().getNhomMau() != null
                    ? tuiMau.getKhoMau().getNhomMau().getDbValue() : "Chưa rõ");
            // Chuyển LocalDateTime sang Date (vì DTO của bạn dùng Date)
            dto.setNgayThuNhan(java.sql.Timestamp.valueOf(tuiMau.getThoiGianLayMau()));
            dto.setTrangThai(tuiMau.getTrangThai().name());
            return dto;
        });
    }

    @Override
    public void deleteBloodUnit(String maTuiMau) {
        // Tìm túi máu
        com.Nhom20.DoAnPhamMem.entity.TuiMauEntity tuiMau = tuiMauRepository.findById(maTuiMau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy túi máu với mã: " + maTuiMau));

        // Đổi trạng thái thành HỦY
        tuiMau.setTrangThai(com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau.HUY);
        tuiMauRepository.save(tuiMau);
    }

    @Override
    public BloodUnitDTO scanBloodUnit(String maTuiMau) {
        // Tìm túi máu khi quét mã vạch
        com.Nhom20.DoAnPhamMem.entity.TuiMauEntity tuiMau = tuiMauRepository.findById(maTuiMau)
                .orElseThrow(() -> new RuntimeException("Túi máu không tồn tại trên hệ thống."));

        // Nếu túi máu đã nhập kho hoặc đã bị hủy thì không cho quét lại
        if (tuiMau.getTrangThai() == com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau.NHAP_KHO) {
            throw new RuntimeException("Túi máu này đã được nhập kho trước đó.");
        }
        if (tuiMau.getTrangThai() == com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau.HUY) {
            throw new RuntimeException("Túi máu này đã bị hủy, không thể nhập kho.");
        }

        // Trả về DTO hiển thị tạm lên bảng
        BloodUnitDTO dto = new BloodUnitDTO();
        dto.setMaTuiMau(tuiMau.getMaTuiMau());
        dto.setNhomMau(tuiMau.getKhoMau() != null && tuiMau.getKhoMau().getNhomMau() != null
                ? tuiMau.getKhoMau().getNhomMau().getDbValue() : "Chưa rõ");
        dto.setNgayThuNhan(java.sql.Timestamp.valueOf(tuiMau.getThoiGianLayMau()));
        dto.setTrangThai(tuiMau.getTrangThai().name());
        return dto;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void importBloodUnits(ImportBloodRequestDTO requestDTO) {
        // 1. Tìm nhân viên thực hiện (người đang đăng nhập / quét mã)
        com.Nhom20.DoAnPhamMem.entity.NhanVienEntity nhanVien = nhanVienRepository.findById(requestDTO.getMaNhanVien())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên thực hiện."));

        // 2. Tạo Phiếu Nhập Xuất mới
        com.Nhom20.DoAnPhamMem.entity.PhieuNhapXuatEntity phieu = new com.Nhom20.DoAnPhamMem.entity.PhieuNhapXuatEntity();
        String maPhieu = "PN" + System.currentTimeMillis() % 100000; // Sinh mã phiếu tạm, bạn có thể tự đổi logic sinh mã
        phieu.setMaPhieu(maPhieu);
        phieu.setNhanVien(nhanVien);
        phieu.setLoaiPhieu("Nhập kho");
        phieu.setNgayNhapXuat(java.time.LocalDate.now());
        phieuNhapXuatRepository.save(phieu);

        // 3. Xử lý từng túi máu được quét
        for (String maTuiMau : requestDTO.getMaTuiMauList()) {
            com.Nhom20.DoAnPhamMem.entity.TuiMauEntity tuiMau = tuiMauRepository.findById(maTuiMau)
                    .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy túi máu " + maTuiMau));

            // a. Tạo Chi tiết phiếu nhập
            com.Nhom20.DoAnPhamMem.entity.ChiTietNhapXuatEntity chiTiet = new com.Nhom20.DoAnPhamMem.entity.ChiTietNhapXuatEntity();
            // Tùy theo cách cấu hình khóa chính kép của ChiTietNhapXuatEntity mà bạn gán cho đúng (VD gán Object hoặc ID)
            chiTiet.setPhieuNhapXuat(phieu);
            chiTiet.setTuiMau(tuiMau);
            chiTietNhapXuatRepository.save(chiTiet);

            // b. Đổi trạng thái túi máu
            tuiMau.setTrangThai(com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau.NHAP_KHO);
            tuiMauRepository.save(tuiMau);

            // c. Tăng số lượng tồn kho lên 1 (Dựa vào kho chứa của túi máu)
            com.Nhom20.DoAnPhamMem.entity.KhoMauEntity khoMau = tuiMau.getKhoMau();
            if (khoMau != null) {
                khoMau.setSoLuongTon(khoMau.getSoLuongTon() + 1);
                khoMauRepository.save(khoMau);
            }
        }
    }

}

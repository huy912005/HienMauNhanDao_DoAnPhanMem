package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.service.TuiMauService;
import org.springframework.stereotype.Service;

@Service
@lombok.RequiredArgsConstructor
public class TuiMauServiceImpl implements TuiMauService {

    private final com.Nhom20.DoAnPhamMem.repository.TuiMauRepository tuiMauRepository;
    private final com.Nhom20.DoAnPhamMem.repository.TinhNguyenVienRepository tinhNguyenVienRepository;
    private final com.Nhom20.DoAnPhamMem.repository.ChienDichHienMauRepository chienDichRepository;
    private final com.Nhom20.DoAnPhamMem.repository.KetQuaLamSangRepository ketQuaLamSangRepository;

    @Override
    public com.Nhom20.DoAnPhamMem.dto.response.DashboardStatsDTO getDashboardStats() {
        com.Nhom20.DoAnPhamMem.dto.response.DashboardStatsDTO stats = new com.Nhom20.DoAnPhamMem.dto.response.DashboardStatsDTO();
        stats.setTotalBloodUnits((int) tuiMauRepository.countByTrangThai(com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau.NHAP_KHO));
        stats.setNewVolunteers((int) tinhNguyenVienRepository.count());
        stats.setActiveCampaigns((int) chienDichRepository.count());
        
        long totalKham = ketQuaLamSangRepository.count();
        long totalDat = ketQuaLamSangRepository.findAll().stream().filter(k -> k.getKetQua() != null && k.getKetQua()).count();
        double passRate = totalKham == 0 ? 0 : ((double) totalDat / totalKham) * 100;
        stats.setScreeningPassRate(Math.round(passRate * 10.0) / 10.0);
        return stats;
    }

    @Override
    public java.util.List<com.Nhom20.DoAnPhamMem.dto.response.MonthlyCollectionStatDTO> getBloodCollectionByMonth(int year) {
        return tuiMauRepository.countBloodUnitsByMonth(year);
    }

    @Override
    public org.springframework.data.domain.Page<com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO> getBloodUnits(int page, int size, String search, String bloodType) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        com.Nhom20.DoAnPhamMem.enums.NhomMau enumNhomMau = null;
        if (bloodType != null && !bloodType.isEmpty()) {
            enumNhomMau = com.Nhom20.DoAnPhamMem.enums.NhomMau.fromDbValue(bloodType);
        }
        return tuiMauRepository.searchAndFilterBloodUnits(search, enumNhomMau, pageable).map(tuiMau -> {
            com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO dto = new com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO();
            dto.setMaTuiMau(tuiMau.getMaTuiMau());
            dto.setNhomMau(tuiMau.getKhoMau() != null && tuiMau.getKhoMau().getNhomMau() != null
                    ? tuiMau.getKhoMau().getNhomMau().getDbValue() : "Chưa rõ");
            // Ngày thu nhận
            dto.setNgayThuNhan(tuiMau.getThoiGianLayMau() != null
                    ? java.sql.Timestamp.valueOf(tuiMau.getThoiGianLayMau()) : null);
            // Thể tích (ml)
            dto.setTheTich(tuiMau.getTheTich() != null ? tuiMau.getTheTich().getMl() : null);
            // Trạng thái hiển thị tiếng Việt
            dto.setTrangThai("Nhập kho");
            // Máu cất đông có hạn sử dụng lâu (VD: 365 ngày thay vì 42 ngày)
            if (tuiMau.getThoiGianLayMau() != null) {
                java.time.LocalDateTime hetHan = tuiMau.getThoiGianLayMau().plusDays(365);
                dto.setNgayHetHan(java.sql.Timestamp.valueOf(hetHan));
                // Tình trạng HSD
                long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDateTime.now(), hetHan);
                if (daysLeft < 0) dto.setTinhTrangHSD("Hết hạn");
                else if (daysLeft <= 30) dto.setTinhTrangHSD("Sắp hết hạn");
                else dto.setTinhTrangHSD("Còn hạn");
            }
            return dto;
        });

    }

    @Override
    public void deleteBloodUnit(String maTuiMau) {
        com.Nhom20.DoAnPhamMem.entity.TuiMauEntity tuiMau = tuiMauRepository.findById(maTuiMau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy túi máu với mã: " + maTuiMau));
        tuiMau.setTrangThai(com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau.HUY);
        tuiMauRepository.save(tuiMau);
    }

    @Override
    public com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO scanBloodUnit(String maTuiMau) {
        com.Nhom20.DoAnPhamMem.entity.TuiMauEntity tuiMau = tuiMauRepository.findById(maTuiMau)
                .orElseThrow(() -> new RuntimeException("Túi máu không tồn tại trên hệ thống."));
        if (tuiMau.getTrangThai() == com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau.NHAP_KHO) {
            throw new RuntimeException("Túi máu này đã được nhập kho trước đó.");
        }
        if (tuiMau.getTrangThai() == com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau.HUY) {
            throw new RuntimeException("Túi máu này đã bị hủy, không thể nhập kho.");
        }
        com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO dto = new com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO();
        dto.setMaTuiMau(tuiMau.getMaTuiMau());
        dto.setNhomMau(tuiMau.getKhoMau() != null && tuiMau.getKhoMau().getNhomMau() != null
                ? tuiMau.getKhoMau().getNhomMau().getDbValue() : "Chưa rõ");
        dto.setNgayThuNhan(tuiMau.getThoiGianLayMau() != null
                ? java.sql.Timestamp.valueOf(tuiMau.getThoiGianLayMau()) : null);
        dto.setTheTich(tuiMau.getTheTich() != null ? tuiMau.getTheTich().getMl() : null);
        dto.setTrangThai(tuiMau.getTrangThai() != null ? tuiMau.getTrangThai().name() : "");
        if (tuiMau.getThoiGianLayMau() != null) {
            java.time.LocalDateTime hetHan = tuiMau.getThoiGianLayMau().plusDays(365);
            dto.setNgayHetHan(java.sql.Timestamp.valueOf(hetHan));
            long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDateTime.now(), hetHan);
            if (daysLeft < 0) dto.setTinhTrangHSD("Hết hạn");
            else if (daysLeft <= 30) dto.setTinhTrangHSD("Sắp hết hạn");
            else dto.setTinhTrangHSD("Còn hạn");
        }
        return dto;
    }
}

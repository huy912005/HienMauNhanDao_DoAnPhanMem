package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.dto.response.*;
import com.Nhom20.DoAnPhamMem.entity.TuiMauEntity;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau;
import com.Nhom20.DoAnPhamMem.enums.NhomMau;
import com.Nhom20.DoAnPhamMem.mapper.TuiMauMapper;
import com.Nhom20.DoAnPhamMem.repository.*;
import com.Nhom20.DoAnPhamMem.service.TuiMauService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TuiMauServiceImpl implements TuiMauService {

    private final TuiMauRepository tuiMauRepository;
    private final TinhNguyenVienRepository tinhNguyenVienRepository;
    private final ChienDichHienMauRepository chienDichRepository;
    private final KetQuaLamSangRepository ketQuaLamSangRepository;
    private final TuiMauMapper tuiMauMapper;

    // --- My Features (Dashboard & Inventory) ---

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalBloodUnits((int) tuiMauRepository.countByTrangThai(TrangThaiTuiMau.NHAP_KHO));
        stats.setNewVolunteers((int) tinhNguyenVienRepository.count());
        stats.setActiveCampaigns((int) chienDichRepository.count());
        
        long totalKham = ketQuaLamSangRepository.count();
        long totalDat = ketQuaLamSangRepository.findAll().stream()
                .filter(k -> k.getKetQua() != null && k.getKetQua()).count();
        double passRate = totalKham == 0 ? 0 : ((double) totalDat / totalKham) * 100;
        stats.setScreeningPassRate(Math.round(passRate * 10.0) / 10.0);
        return stats;
    }

    @Override
    public List<MonthlyCollectionStatDTO> getBloodCollectionByMonth(int year) {
        return tuiMauRepository.countBloodUnitsByMonth(year);
    }

    @Override
    public Page<BloodUnitDTO> getBloodUnits(int page, int size, String search, String bloodType) {
        Pageable pageable = PageRequest.of(page, size);
        NhomMau enumNhomMau = null;
        if (bloodType != null && !bloodType.isEmpty()) {
            enumNhomMau = NhomMau.fromDbValue(bloodType);
        }
        return tuiMauRepository.searchAndFilterBloodUnits(search, enumNhomMau, pageable).map(tuiMau -> {
            BloodUnitDTO dto = new BloodUnitDTO();
            dto.setMaTuiMau(tuiMau.getMaTuiMau());
            dto.setNhomMau(tuiMau.getKhoMau() != null && tuiMau.getKhoMau().getNhomMau() != null
                    ? tuiMau.getKhoMau().getNhomMau().getDbValue() : "Chưa rõ");
            dto.setNgayThuNhan(tuiMau.getThoiGianLayMau() != null
                    ? Timestamp.valueOf(tuiMau.getThoiGianLayMau()) : null);
            dto.setTheTich(tuiMau.getTheTich() != null ? tuiMau.getTheTich().getMl() : null);
            dto.setTrangThai("Nhập kho");
            
            if (tuiMau.getThoiGianLayMau() != null) {
                LocalDateTime hetHan = tuiMau.getThoiGianLayMau().plusDays(365);
                dto.setNgayHetHan(Timestamp.valueOf(hetHan));
                long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), hetHan);
                if (daysLeft < 0) dto.setTinhTrangHSD("Hết hạn");
                else if (daysLeft <= 30) dto.setTinhTrangHSD("Sắp hết hạn");
                else dto.setTinhTrangHSD("Còn hạn");
            }
            return dto;
        });
    }

    @Override
    public void deleteBloodUnit(String maTuiMau) {
        TuiMauEntity tuiMau = tuiMauRepository.findById(maTuiMau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy túi máu với mã: " + maTuiMau));
        tuiMau.setTrangThai(TrangThaiTuiMau.HUY);
        tuiMauRepository.save(tuiMau);
    }

    @Override
    public BloodUnitDTO scanBloodUnit(String maTuiMau) {
        TuiMauEntity tuiMau = tuiMauRepository.findById(maTuiMau)
                .orElseThrow(() -> new RuntimeException("Túi máu không tồn tại trên hệ thống."));
        if (tuiMau.getTrangThai() == TrangThaiTuiMau.NHAP_KHO) {
            throw new RuntimeException("Túi máu này đã được nhập kho trước đó.");
        }
        if (tuiMau.getTrangThai() == TrangThaiTuiMau.HUY) {
            throw new RuntimeException("Túi máu này đã bị hủy, không thể nhập kho.");
        }
        BloodUnitDTO dto = new BloodUnitDTO();
        dto.setMaTuiMau(tuiMau.getMaTuiMau());
        dto.setNhomMau(tuiMau.getKhoMau() != null && tuiMau.getKhoMau().getNhomMau() != null
                ? tuiMau.getKhoMau().getNhomMau().getDbValue() : "Chưa rõ");
        dto.setNgayThuNhan(tuiMau.getThoiGianLayMau() != null
                ? Timestamp.valueOf(tuiMau.getThoiGianLayMau()) : null);
        dto.setTheTich(tuiMau.getTheTich() != null ? tuiMau.getTheTich().getMl() : null);
        dto.setTrangThai(tuiMau.getTrangThai() != null ? tuiMau.getTrangThai().name() : "");
        if (tuiMau.getThoiGianLayMau() != null) {
            LocalDateTime hetHan = tuiMau.getThoiGianLayMau().plusDays(365);
            dto.setNgayHetHan(Timestamp.valueOf(hetHan));
            long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), hetHan);
            if (daysLeft < 0) dto.setTinhTrangHSD("Hết hạn");
            else if (daysLeft <= 30) dto.setTinhTrangHSD("Sắp hết hạn");
            else dto.setTinhTrangHSD("Còn hạn");
        }
        return dto;
    }

    // --- Develop Features ---

    @Override
    public List<TuiMauResponse> getAll() {
        return tuiMauRepository.findAll().stream()
                .map(tuiMauMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CollectionStatsResponse getStats() {
        List<TuiMauEntity> all = tuiMauRepository.findAll();
        long tongSoTui = all.size();
        double tongTheTich = all.stream()
                .mapToDouble(tm -> tm.getTheTich() != null ? tm.getTheTich().getMl() : 0)
                .sum();

        Map<String, Long> theoNhomMau = all.stream()
                .filter(tm -> tm.getDonDangKy() != null && tm.getDonDangKy().getTinhNguyenVien() != null
                        && tm.getDonDangKy().getTinhNguyenVien().getNhomMau() != null)
                .collect(Collectors.groupingBy(
                        tm -> tm.getDonDangKy().getTinhNguyenVien().getNhomMau().getDbValue(),
                        Collectors.counting()));

        Map<String, Long> theoTheTich = all.stream()
                .filter(tm -> tm.getTheTich() != null)
                .collect(Collectors.groupingBy(tm -> String.valueOf(tm.getTheTich().getMl()),
                        Collectors.counting()));

        return CollectionStatsResponse.builder()
                .tongSoTui(tongSoTui)
                .tongTheTich(tongTheTich)
                .theoNhomMau(theoNhomMau)
                .theoTheTich(theoTheTich)
                .build();
    }

    @Override
    public void delete(String maTuiMau) {
        tuiMauRepository.deleteById(maTuiMau);
    }

    @Override
    public void updateStatus(String maTuiMau, String status) {
        tuiMauRepository.findById(maTuiMau).ifPresent(tuiMau -> {
            tuiMau.setTrangThai(TrangThaiTuiMau.fromDbValue(status));
            tuiMauRepository.save(tuiMau);
        });
    }
}

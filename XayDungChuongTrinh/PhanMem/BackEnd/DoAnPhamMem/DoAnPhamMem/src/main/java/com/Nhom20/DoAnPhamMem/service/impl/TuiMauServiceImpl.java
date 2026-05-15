package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.dto.response.*;
import com.Nhom20.DoAnPhamMem.dto.request.TuiMauRequest;
import com.Nhom20.DoAnPhamMem.entity.*;
import com.Nhom20.DoAnPhamMem.enums.*;
import com.Nhom20.DoAnPhamMem.mapper.TuiMauMapper;
import com.Nhom20.DoAnPhamMem.repository.*;
import com.Nhom20.DoAnPhamMem.service.TuiMauService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class TuiMauServiceImpl implements TuiMauService {

    private final TuiMauRepository tuiMauRepository;
    private final TinhNguyenVienRepository tinhNguyenVienRepository;
    private final ChienDichHienMauRepository chienDichRepository;
    private final KetQuaLamSangRepository ketQuaLamSangRepository;
    private final DonDangKyRepository donDangKyRepository;
    private final NhanVienRepository nhanVienRepository;
    private final KhoMauRepository khoMauRepository;
    private final TuiMauMapper tuiMauMapper;

    // --- My Features (Dashboard & Inventory) ---

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalBloodUnits((int) tuiMauRepository.countAvailableUnits());
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
    public Page<BloodUnitDTO> getBloodUnits(int page, int size, String search, String bloodType, String maChienDich) {
        Pageable pageable = PageRequest.of(page, size);
        NhomMau enumNhomMau = null;
        if (bloodType != null && !bloodType.isEmpty()) {
            enumNhomMau = NhomMau.fromDbValue(bloodType);
        }
        return tuiMauRepository.searchAndFilterBloodUnits(search, enumNhomMau, maChienDich, pageable).map(tuiMau -> {
            BloodUnitDTO dto = tuiMauMapper.toBloodUnitDTO(tuiMau);
            
            // Tính toán bổ sung các trường Hạn sử dụng
            if (tuiMau.getThoiGianLayMau() != null) {
                LocalDateTime hetHan = tuiMau.getThoiGianLayMau().plusDays(365);
                dto.setNgayHetHan(Timestamp.valueOf(hetHan));
                long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), hetHan);
                if (daysLeft < 0)
                    dto.setTinhTrangHSD("Hết hạn");
                else if (daysLeft <= 30)
                    dto.setTinhTrangHSD("Sắp hết hạn");
                else
                    dto.setTinhTrangHSD("Còn hạn");
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
                ? tuiMau.getKhoMau().getNhomMau().getDbValue()
                : "Chưa rõ");
        dto.setNgayThuNhan(tuiMau.getThoiGianLayMau() != null
                ? Timestamp.valueOf(tuiMau.getThoiGianLayMau())
                : null);
        dto.setTheTich(tuiMau.getTheTich() != null ? tuiMau.getTheTich().getMl() : null);
        dto.setTrangThai(tuiMau.getTrangThai() != null ? tuiMau.getTrangThai().name() : "");
        if (tuiMau.getThoiGianLayMau() != null) {
            LocalDateTime hetHan = tuiMau.getThoiGianLayMau().plusDays(365);
            dto.setNgayHetHan(Timestamp.valueOf(hetHan));
            long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), hetHan);
            if (daysLeft < 0)
                dto.setTinhTrangHSD("Hết hạn");
            else if (daysLeft <= 30)
                dto.setTinhTrangHSD("Sắp hết hạn");
            else
                dto.setTinhTrangHSD("Còn hạn");
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
        System.out.println("DEBUG: Dang cap nhat trang thai tui mau [" + maTuiMau + "] sang [" + status + "]");
        tuiMauRepository.findById(maTuiMau).ifPresentOrElse(tuiMau -> {
            try {
                TrangThaiTuiMau newTrangThai = TrangThaiTuiMau.fromDbValue(status);
                tuiMau.setTrangThai(newTrangThai);
                tuiMauRepository.save(tuiMau);
                System.out.println("DEBUG: Cap nhat thanh cong tui mau [" + maTuiMau + "]");
            } catch (Exception e) {
                System.err.println("DEBUG: Loi khi tim trang thai enum cho [" + status + "]: " + e.getMessage());
                throw e;
            }
        }, () -> {
            System.err.println("DEBUG: Khong tim thay tui mau [" + maTuiMau + "]");
            throw new RuntimeException("Không tìm thấy túi máu: " + maTuiMau);
        });
    }

    @Override
    @Transactional
    public void updateTuiMau(String maTuiMau, TuiMauRequest request) {
        TuiMauEntity tuiMau = tuiMauRepository.findById(maTuiMau)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy túi máu: " + maTuiMau));
        
        if (request.getTheTich() != null) {
            tuiMau.setTheTich(TheTichTuiMau.fromMl(request.getTheTich()));
        }
        if (request.getThoiGianLayMau() != null) {
            tuiMau.setThoiGianLayMau(request.getThoiGianLayMau());
        }
        if (request.getNhietDoVanChuyen() != null) {
            tuiMau.setNhietDoVanChuyen(request.getNhietDoVanChuyen());
        }
        
        // Nếu đang là trạng thái Hủy mà sửa thông tin -> Khôi phục về Chờ xét nghiệm
        if (tuiMau.getTrangThai() == TrangThaiTuiMau.HUY) {
            tuiMau.setTrangThai(TrangThaiTuiMau.CHO_XET_NGHIEM);
            
            // Khôi phục trạng thái đơn đăng ký thành Đã hiến
            if (tuiMau.getDonDangKy() != null) {
                DonDangKyEntity don = tuiMau.getDonDangKy();
                don.setTrangThai(TrangThaiDonDangKy.DA_HIEN);
                // Thể tích sẽ được cập nhật ở đoạn code phía dưới
                donDangKyRepository.save(don);
            }
        }
        
        tuiMauRepository.save(tuiMau);
        
        // Cập nhật lại thể tích trong đơn đăng ký nếu túi máu không phải trạng thái Hủy (hoặc vừa được khôi phục)
        if (tuiMau.getTrangThai() != TrangThaiTuiMau.HUY && tuiMau.getDonDangKy() != null && request.getTheTich() != null) {
            DonDangKyEntity don = tuiMau.getDonDangKy();
            don.setTheTich(TheTich.fromDbValue(request.getTheTich()));
            donDangKyRepository.save(don);
        }
    }

    @Override
    @Transactional
    public void createTuiMau(TuiMauRequest request) {
        // 1. Tìm đơn đăng ký
        DonDangKyEntity don = donDangKyRepository.findById(request.getMaDon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đăng ký: " + request.getMaDon()));

        // 2. Tìm nhân viên
        NhanVienEntity nv = nhanVienRepository.findById(request.getMaNV())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên: " + request.getMaNV()));

        // 3. Tìm kho máu tương ứng với nhóm máu của TNV
        TinhNguyenVienEntity tnv = don.getTinhNguyenVien();
        KhoMauEntity kho = khoMauRepository.findByNhomMau(tnv.getNhomMau())
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy kho máu cho nhóm máu: " + tnv.getNhomMau().getDbValue()));

        // 4. Sinh mã túi máu mới
        Integer maxId = tuiMauRepository.findMaxMaTuiMau();
        String nextMaTuiMau = "TM" + String.format("%05d", (maxId != null ? maxId + 1 : 1));

        // 5. Tạo thực thể túi máu
        TuiMauEntity tuiMau = new TuiMauEntity();
        tuiMau.setMaTuiMau(nextMaTuiMau);
        tuiMau.setDonDangKy(don);
        tuiMau.setNhanVien(nv);
        tuiMau.setKhoMau(kho);
        tuiMau.setTheTich(TheTichTuiMau.fromMl(request.getTheTich()));
        tuiMau.setThoiGianLayMau(request.getThoiGianLayMau());
        tuiMau.setTrangThai(TrangThaiTuiMau.CHO_XET_NGHIEM);
        tuiMau.setNhietDoVanChuyen(request.getNhietDoVanChuyen());

        tuiMauRepository.save(tuiMau);

        // 6. Cập nhật Đơn đăng ký thành Đã hiến và set thể tích
        don.setTrangThai(TrangThaiDonDangKy.DA_HIEN);
        don.setTheTich(TheTich.fromDbValue(request.getTheTich()));
        donDangKyRepository.save(don);
    }

    @Override
    public List<BloodExpiryDTO> getExpiryManagementData(String viewMode, String search) {
        List<TuiMauEntity> units = tuiMauRepository.findAllAvailableUnits();
        LocalDateTime now = LocalDateTime.now();

        return units.stream()
                .map(t -> {
                    BloodExpiryDTO dto = new BloodExpiryDTO();
                    dto.setMaTuiMau(t.getMaTuiMau());
                    dto.setMaChienDich(t.getDonDangKy() != null && t.getDonDangKy().getChienDich() != null
                            ? t.getDonDangKy().getChienDich().getMaChienDich()
                            : "N/A");
                    dto.setNhomMau(t.getKhoMau() != null && t.getKhoMau().getNhomMau() != null
                            ? t.getKhoMau().getNhomMau().getDbValue()
                            : "Chưa rõ");
                    dto.setTheTich(t.getTheTich() != null ? t.getTheTich().getMl() : 0);
                    dto.setThoiGianLayMau(t.getThoiGianLayMau());

                    if (t.getThoiGianLayMau() != null) {
                        LocalDateTime hetHan = t.getThoiGianLayMau().plusDays(365); // 365 ngày theo yêu cầu
                        dto.setNgayHetHan(hetHan);
                        long daysRemaining = ChronoUnit.DAYS.between(now, hetHan);
                        dto.setDaysRemaining(daysRemaining);

                        if (daysRemaining < -30) {
                            dto.setTrangThaiHan("ARCHIVED_EXPIRED"); // Quá hạn trên 30 ngày -> Làm mờ
                        } else if (daysRemaining <= -20) {
                            dto.setTrangThaiHan("WARNING_EXPIRED"); // Quá hạn 20-30 ngày -> Chớp đỏ
                        } else if (daysRemaining < 0) {
                            dto.setTrangThaiHan("EXPIRED");
                        } else if (daysRemaining <= 7) {
                            dto.setTrangThaiHan("NEAR_EXPIRY");
                        } else {
                            dto.setTrangThaiHan("SAFE");
                        }
                    }
                    return dto;
                })
                .filter(dto -> {
                    // Filter by search
                    if (search != null && !search.isEmpty()) {
                        return dto.getMaTuiMau().toLowerCase().contains(search.toLowerCase());
                    }
                    return true;
                })
                .filter(dto -> {
                    // Filter by viewMode
                    if ("expired".equalsIgnoreCase(viewMode)) {
                        return "EXPIRED".equals(dto.getTrangThaiHan()) 
                            || "WARNING_EXPIRED".equals(dto.getTrangThaiHan())
                            || "ARCHIVED_EXPIRED".equals(dto.getTrangThaiHan());
                    }
                    if ("near".equalsIgnoreCase(viewMode)) return "NEAR_EXPIRY".equals(dto.getTrangThaiHan());
                    if ("safe".equalsIgnoreCase(viewMode)) return "SAFE".equals(dto.getTrangThaiHan());
                    return true;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ExpiryStatsDTO getExpiryStats() {
        List<BloodExpiryDTO> allData = getExpiryManagementData("all", null);
        ExpiryStatsDTO stats = new ExpiryStatsDTO();
        stats.setExpiredCount(allData.stream().filter(d -> 
            "EXPIRED".equals(d.getTrangThaiHan()) 
            || "WARNING_EXPIRED".equals(d.getTrangThaiHan())
            || "ARCHIVED_EXPIRED".equals(d.getTrangThaiHan())
        ).count());
        stats.setNearExpiryCount(allData.stream().filter(d -> "NEAR_EXPIRY".equals(d.getTrangThaiHan())).count());
        stats.setSafeCount(allData.stream().filter(d -> "SAFE".equals(d.getTrangThaiHan())).count());
        
        // Kiểm tra xem toàn hệ thống có túi nào đang ở mức cảnh báo 20-30 ngày không
        boolean hasCritical = allData.stream().anyMatch(d -> "WARNING_EXPIRED".equals(d.getTrangThaiHan()));
        stats.setHasCritical(hasCritical);
        
        return stats;
    }

    @Override
    @Transactional
    public void deleteExpiredUnits() {
        List<BloodExpiryDTO> expired = getExpiryManagementData("expired", null);
        List<String> ids = expired.stream().map(BloodExpiryDTO::getMaTuiMau).collect(Collectors.toList());
        if (!ids.isEmpty()) {
            tuiMauRepository.deleteByMaTuiMauIn(ids);
        }
    }
}

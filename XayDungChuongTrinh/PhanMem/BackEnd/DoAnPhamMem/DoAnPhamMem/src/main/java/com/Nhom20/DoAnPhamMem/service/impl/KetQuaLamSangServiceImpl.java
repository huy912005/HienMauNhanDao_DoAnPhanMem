package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.dto.request.KetQuaLamSangRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KetQuaLamSangResponse;
import com.Nhom20.DoAnPhamMem.dto.response.ScreeningStatsResponse;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.entity.KetQuaLamSangEntity;
import com.Nhom20.DoAnPhamMem.entity.TuiMauEntity;
import com.Nhom20.DoAnPhamMem.entity.TinhNguyenVienEntity;
import com.Nhom20.DoAnPhamMem.enums.TheTich;
import com.Nhom20.DoAnPhamMem.enums.TheTichTuiMau;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau;
import com.Nhom20.DoAnPhamMem.mapper.KetQuaLamSangMapper;
import com.Nhom20.DoAnPhamMem.repository.DonDangKyRepository;
import com.Nhom20.DoAnPhamMem.repository.KetQuaLamSangRepository;
import com.Nhom20.DoAnPhamMem.repository.NhanVienRepository;
import com.Nhom20.DoAnPhamMem.repository.TuiMauRepository;
import com.Nhom20.DoAnPhamMem.service.KetQuaLamSangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KetQuaLamSangServiceImpl implements KetQuaLamSangService {

    private final KetQuaLamSangRepository ketQuaLamSangRepository;
    private final KetQuaLamSangMapper ketQuaLamSangMapper;
    private final DonDangKyRepository donDangKyRepository;
    private final NhanVienRepository nhanVienRepository;
    private final TuiMauRepository tuiMauRepository;

    @Transactional(readOnly = true)
    public List<String> getMaDonDaKham() {
        return ketQuaLamSangRepository.findAllMaDonDaKham();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KetQuaLamSangResponse> getAll() {
        return ketQuaLamSangRepository.findAll().stream()
                .map(ketQuaLamSangMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<KetQuaLamSangResponse> getWaiting() {
        // Lấy tất cả đơn ở trạng thái Đã đăng ký, Chờ khám hoặc Chưa hiến
        return donDangKyRepository.findAll().stream()
                .filter(don -> don.getTrangThai() == TrangThaiDonDangKy.DA_DANG_KY ||
                        don.getTrangThai() == TrangThaiDonDangKy.CHO_KHAM ||
                        don.getTrangThai() == TrangThaiDonDangKy.CHUA_HIEN)
                .map(don -> {
                    TinhNguyenVienEntity tnv = don.getTinhNguyenVien();
                    return KetQuaLamSangResponse.builder()
                            .maDon(don.getMaDon())
                            .tenTinhNguyenVien(tnv != null ? tnv.getHoTen() : null)
                            .ngaySinh(tnv != null && tnv.getNgaySinh() != null ? tnv.getNgaySinh().toString() : null)
                            .gioiTinh(tnv != null && tnv.getGioiTinh() != null ? tnv.getGioiTinh().getDbValue() : null)
                            .nhomMau(tnv != null && tnv.getNhomMau() != null ? tnv.getNhomMau().getDbValue() : null)
                            .tenChienDich(don.getChienDich() != null ? don.getChienDich().getTenChienDich() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ScreeningStatsResponse getStats() {
        List<KetQuaLamSangEntity> all = ketQuaLamSangRepository.findAll();
        long tongSo = all.size();
        long datYeuCau = all.stream().filter(kq -> Boolean.TRUE.equals(kq.getKetQua())).count();
        long khongDat = tongSo - datYeuCau;

        return ScreeningStatsResponse.builder()
                .tongSo(tongSo)
                .datYeuCau(datYeuCau)
                .khongDat(khongDat)
                .build();
    }

    @Override
    @Transactional
    public void save(KetQuaLamSangRequest request) {
        DonDangKyEntity don = donDangKyRepository.findById(request.getMaDon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đăng ký: " + request.getMaDon()));

        KetQuaLamSangEntity entity = new KetQuaLamSangEntity();
        // Sinh mã KQ theo format: tìm max số, tách prefix "KQ" và số, cộng 1
        Integer maxNum = ketQuaLamSangRepository.findMaxMaKQ();
        int nextNum = (maxNum != null ? maxNum : 0) + 1;
        entity.setMaKQ("KL" + String.format("%05d", nextNum));
        entity.setDonDangKy(don);
        entity.setBacSiKham(nhanVienRepository.findById(request.getMaNhanVien()).orElse(null));
        entity.setHuyetAp(request.getHuyetAp());
        entity.setNhipTim(request.getNhipTim());
        entity.setCanNang(request.getCanNang());
        entity.setNhietDo(request.getNhietDo());
        entity.setKetQua(request.getKetQua());
        entity.setLyDoTuChoi(request.getLyDoTuChoi());

        ketQuaLamSangRepository.save(entity);

        // Không cập nhật DonDangKy ở đây để tránh vi phạm Ràng buộc (Constraint) của DB
        // Chỉ cần lưu KetQuaLamSang là đủ để danh sách Thu nhận máu hiển thị được
    }

    @Override
    public void delete(String maKQ) {
        ketQuaLamSangRepository.deleteById(maKQ);
    }

    @Override
    @Transactional
    public void update(String maKQ, KetQuaLamSangRequest request) {
        String id = maKQ != null ? maKQ.trim() : "";
        KetQuaLamSangEntity entity = ketQuaLamSangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kết quả khám: " + id));
        if (request.getMaNhanVien() != null && !request.getMaNhanVien().isBlank()) {
            nhanVienRepository.findById(request.getMaNhanVien().trim()).ifPresent(entity::setBacSiKham);
        }
        if (request.getCanNang() != null && request.getCanNang() < 40) {
            throw new IllegalArgumentException("Cân nặng phải từ 40 kg trở lên (ràng buộc CSDL).");
        }
        entity.setHuyetAp(request.getHuyetAp());
        entity.setNhipTim(request.getNhipTim());
        entity.setCanNang(request.getCanNang());
        entity.setNhietDo(request.getNhietDo());
        if (request.getKetQua() != null) {
            entity.setKetQua(Boolean.TRUE.equals(request.getKetQua()));
        }
        entity.setLyDoTuChoi(Boolean.TRUE.equals(entity.getKetQua()) ? null : request.getLyDoTuChoi());
        ketQuaLamSangRepository.save(entity);
    }
}

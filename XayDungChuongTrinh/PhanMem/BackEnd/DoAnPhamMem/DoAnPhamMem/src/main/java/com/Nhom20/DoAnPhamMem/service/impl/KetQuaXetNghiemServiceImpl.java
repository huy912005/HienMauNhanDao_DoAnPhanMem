package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.dto.request.KetQuaXetNghiemRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KetQuaXetNghiemResponse;
import com.Nhom20.DoAnPhamMem.entity.KetQuaXetNghiemEntity;
import com.Nhom20.DoAnPhamMem.entity.NhanVienEntity;
import com.Nhom20.DoAnPhamMem.entity.TuiMauEntity;
import com.Nhom20.DoAnPhamMem.enums.NhomMau;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau;
import com.Nhom20.DoAnPhamMem.repository.KetQuaXetNghiemRepository;
import com.Nhom20.DoAnPhamMem.repository.NhanVienRepository;
import com.Nhom20.DoAnPhamMem.repository.TuiMauRepository;
import com.Nhom20.DoAnPhamMem.service.KetQuaXetNghiemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KetQuaXetNghiemServiceImpl implements KetQuaXetNghiemService {

    private final KetQuaXetNghiemRepository ketQuaXetNghiemRepository;
    private final TuiMauRepository tuiMauRepository;
    private final NhanVienRepository nhanVienRepository;

    @Override
    @Transactional(readOnly = true)
    public List<KetQuaXetNghiemResponse> getAll() {
        return ketQuaXetNghiemRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public KetQuaXetNghiemResponse create(KetQuaXetNghiemRequest request) {
        TuiMauEntity tuiMau = tuiMauRepository.findById(request.getMaTuiMau())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy túi máu: " + request.getMaTuiMau()));
        Integer maxNum = ketQuaXetNghiemRepository.findMaxMaKQ();
        int nextNum = (maxNum != null ? maxNum : 0) + 1;
        String maKQ = "XN" + String.format("%05d", nextNum);
        KetQuaXetNghiemEntity entity = new KetQuaXetNghiemEntity();
        entity.setMaKQ(maKQ);
        entity.setTuiMau(tuiMau);
        entity.setNhanVienXetNghiem(null);
        ketQuaXetNghiemRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<KetQuaXetNghiemResponse> getByMaTuiMau(String maTuiMau) {
        return ketQuaXetNghiemRepository.findByTuiMau_MaTuiMau(maTuiMau).map(this::toResponse);
    }

    @Override
    @Transactional
    public KetQuaXetNghiemResponse update(String maKQ, KetQuaXetNghiemRequest request) {
        KetQuaXetNghiemEntity entity = ketQuaXetNghiemRepository.findById(maKQ.trim())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kết quả xét nghiệm: " + maKQ));
        // Cập nhật các trường bác sĩ nhập
        if (request.getNhomMau() != null && !request.getNhomMau().isBlank()) {
            entity.setNhomMau(NhomMau.fromDbValue(request.getNhomMau()));
        } else {
            entity.setNhomMau(null);
        }
        if (request.getSoLanXetNghiem() != null) {
            entity.setSoLanXetNghiem(request.getSoLanXetNghiem());
        }
        if (request.getMoTa() != null) {
            entity.setMoTa(request.getMoTa());
        }
        if (request.getKetQua() != null) {
            entity.setKetQua(request.getKetQua());
            // Cập nhật trạng thái túi máu dựa trên kết quả xét nghiệm
            if (entity.getTuiMau() != null) {
                if (Boolean.TRUE.equals(request.getKetQua())) {
                    entity.getTuiMau().setTrangThai(TrangThaiTuiMau.YEU_CAU_NHAP_KHO);
                } else {
                    entity.getTuiMau().setTrangThai(TrangThaiTuiMau.HUY);
                }
                tuiMauRepository.save(entity.getTuiMau());
            }
        }
        if (request.getMaNhanVien() != null && !request.getMaNhanVien().isBlank()) {
            NhanVienEntity nhanVien = nhanVienRepository.findById(request.getMaNhanVien())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên: " + request.getMaNhanVien()));
            entity.setNhanVienXetNghiem(nhanVien);
        }
        ketQuaXetNghiemRepository.save(entity);
        return toResponse(entity);
    }

    private KetQuaXetNghiemResponse toResponse(KetQuaXetNghiemEntity e) {
        return KetQuaXetNghiemResponse.builder()
                .maKQ(e.getMaKQ())
                .maTuiMau(e.getTuiMau() != null ? e.getTuiMau().getMaTuiMau() : null)
                .maNhanVien(e.getNhanVienXetNghiem() != null ? e.getNhanVienXetNghiem().getMaNhanVien() : null)
                .tenNhanVien(e.getNhanVienXetNghiem() != null ? e.getNhanVienXetNghiem().getHoTen() : null)
                .nhomMau(e.getNhomMau() != null ? e.getNhomMau().getDbValue() : null)
                .soLanXetNghiem(e.getSoLanXetNghiem())
                .ketQua(e.getKetQua())
                .moTa(e.getMoTa())
                .build();
    }
}

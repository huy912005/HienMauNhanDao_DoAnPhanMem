package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.dto.ChienDichHienMauDto;
import com.Nhom20.DoAnPhamMem.dto.KhoMauDto;
import com.Nhom20.DoAnPhamMem.dto.TinTucDto;
import com.Nhom20.DoAnPhamMem.dto.TrangChuResponse;
import com.Nhom20.DoAnPhamMem.entity.ChienDichHienMauEntity;
import com.Nhom20.DoAnPhamMem.entity.KhoMauEntity;
import com.Nhom20.DoAnPhamMem.entity.TinTucEntity;
import com.Nhom20.DoAnPhamMem.repository.ChienDichHienMauRepository;
import com.Nhom20.DoAnPhamMem.repository.KhoMauRepository;
import com.Nhom20.DoAnPhamMem.repository.TinTucRepository;
import com.Nhom20.DoAnPhamMem.repository.TuiMauRepository;
import com.Nhom20.DoAnPhamMem.service.TrangChuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrangChuServiceImpl implements TrangChuService {

    private final ChienDichHienMauRepository chienDichHienMauRepository;
    private final KhoMauRepository khoMauRepository;
    private final TinTucRepository tinTucRepository;
    private final TuiMauRepository tuiMauRepository;

    public TrangChuServiceImpl(ChienDichHienMauRepository chienDichHienMauRepository,
                               KhoMauRepository khoMauRepository,
                               TinTucRepository tinTucRepository,
                               TuiMauRepository tuiMauRepository) {
        this.chienDichHienMauRepository = chienDichHienMauRepository;
        this.khoMauRepository = khoMauRepository;
        this.tinTucRepository = tinTucRepository;
        this.tuiMauRepository = tuiMauRepository;
    }

    @Override
    public TrangChuResponse layDuLieuTrangChu() {
        List<ChienDichHienMauDto> dangHoatDong = chienDichHienMauRepository.findAll().stream()
                .filter(cd -> cd.getTrangThai() != null && "Đang diễn ra".equals(cd.getTrangThai().name()))
                .map(this::mapToChienDichDto)
                .collect(Collectors.toList());

        int tongLuongMau = tuiMauRepository.findAll().stream()
                .filter(tm -> tm.getTrangThai() != null && ("Nhập kho".equals(tm.getTrangThai()) || "Đã xuất".equals(tm.getTrangThai())))
                .mapToInt(tm -> tm.getTheTich() != null ? tm.getTheTich() : 0)
                .sum();

        List<KhoMauDto> tonKho = khoMauRepository.findAll().stream()
                .map(this::mapToKhoMauDto)
                .collect(Collectors.toList());
        
        List<TinTucDto> tinTuc = tinTucRepository.findAll().stream()
                .sorted((t1, t2) -> t2.getNgayDang().compareTo(t1.getNgayDang()))
                .limit(5)
                .map(this::mapToTinTucDto)
                .collect(Collectors.toList());

        return TrangChuResponse.builder()
                .chienDichDangHoatDong(dangHoatDong)
                .tongLuongMauDaThu(tongLuongMau)
                .luongMauTonKho(tonKho)
                .hoatDongGanDay(tinTuc)
                .build();
    }

    private ChienDichHienMauDto mapToChienDichDto(ChienDichHienMauEntity entity) {
        return ChienDichHienMauDto.builder()
                .maChienDich(entity.getMaChienDich())
                .tenChienDich(entity.getTenChienDich())
                .thoiGianBD(entity.getThoiGianBD())
                .thoiGianKT(entity.getThoiGianKT())
                .soLuongDuKien(entity.getSoLuongDuKien())
                .trangThai(entity.getTrangThai() != null ? entity.getTrangThai().name() : null)
                .build();
    }

    private KhoMauDto mapToKhoMauDto(KhoMauEntity entity) {
        return KhoMauDto.builder()
                .maKho(entity.getMaKho())
                .nhomMau(entity.getNhomMau() != null ? entity.getNhomMau().name() : null)
                .soLuongTon(entity.getSoLuongTon())
                .nguongAnToan(entity.getNguongAnToan())
                .build();
    }

    private TinTucDto mapToTinTucDto(TinTucEntity entity) {
        return TinTucDto.builder()
                .maTinTuc(entity.getMaTinTuc())
                .tieuDe(entity.getTieuDe())
                .noiDung(entity.getNoiDung())
                .hinhAnh(entity.getHinhAnh())
                .ngayDang(entity.getNgayDang())
                .trangThai(entity.getTrangThai() != null ? entity.getTrangThai().name() : null)
                .build();
    }
}

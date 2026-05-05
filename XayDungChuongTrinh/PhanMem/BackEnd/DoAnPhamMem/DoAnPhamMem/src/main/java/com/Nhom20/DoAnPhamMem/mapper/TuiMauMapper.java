package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.request.TuiMauRequest;
import com.Nhom20.DoAnPhamMem.dto.response.TuiMauResponse;
import com.Nhom20.DoAnPhamMem.entity.TuiMauEntity;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.entity.NhanVienEntity;
import org.springframework.stereotype.Component;

@Component
public class TuiMauMapper {

    public TuiMauEntity toEntity(TuiMauRequest request) {
        if (request == null) return null;
        TuiMauEntity entity = new TuiMauEntity();
        entity.setMaTuiMau(request.getMaTuiMau());
        
        if (request.getMaDon() != null) {
            DonDangKyEntity don = new DonDangKyEntity();
            don.setMaDon(request.getMaDon());
            entity.setDonDangKy(don);
        }
        
        if (request.getMaNhanVien() != null) {
            NhanVienEntity nv = new NhanVienEntity();
            nv.setMaNhanVien(request.getMaNhanVien());
            entity.setNhanVien(nv);
        }
        
        entity.setMaKho(request.getMaKho());
        entity.setTheTich(request.getTheTich());
        entity.setThoiGianLayMau(request.getThoiGianLayMau());
        entity.setTrangThai(request.getTrangThai());
        entity.setNhietDoVanChuyen(request.getNhietDoVanChuyen());
        return entity;
    }

    public TuiMauResponse toResponse(TuiMauEntity entity) {
        if (entity == null) return null;
        return TuiMauResponse.builder()
                .maTuiMau(entity.getMaTuiMau())
                .maDon(entity.getDonDangKy() != null ? entity.getDonDangKy().getMaDon() : null)
                .maNhanVien(entity.getNhanVien() != null ? entity.getNhanVien().getMaNhanVien() : null)
                .maKho(entity.getMaKho())
                .theTich(entity.getTheTich())
                .thoiGianLayMau(entity.getThoiGianLayMau())
                .trangThai(entity.getTrangThai())
                .nhietDoVanChuyen(entity.getNhietDoVanChuyen())
                .build();
    }
}
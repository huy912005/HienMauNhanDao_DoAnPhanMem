package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.request.KetQuaLamSangRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KetQuaLamSangResponse;
import com.Nhom20.DoAnPhamMem.entity.KetQuaLamSangEntity;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.entity.NhanVienEntity;
import org.springframework.stereotype.Component;

@Component
public class KetQuaLamSangMapper {

    public KetQuaLamSangEntity toEntity(KetQuaLamSangRequest request) {
        if (request == null) return null;
        KetQuaLamSangEntity entity = new KetQuaLamSangEntity();
        entity.setMaKQ(request.getMaKQ());
        
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
        
        entity.setHuyetAp(request.getHuyetAp());
        entity.setNhipTim(request.getNhipTim());
        entity.setCanNang(request.getCanNang());
        entity.setNhietDo(request.getNhietDo());
        entity.setKetQua(request.getKetQua());
        entity.setLyDoTuChoi(request.getLyDoTuChoi());
        return entity;
    }

    public KetQuaLamSangResponse toResponse(KetQuaLamSangEntity entity) {
        if (entity == null) return null;
        return KetQuaLamSangResponse.builder()
                .maKQ(entity.getMaKQ())
                .maDon(entity.getDonDangKy() != null ? entity.getDonDangKy().getMaDon() : null)
                .maNhanVien(entity.getNhanVien() != null ? entity.getNhanVien().getMaNhanVien() : null)
                .huyetAp(entity.getHuyetAp())
                .nhipTim(entity.getNhipTim())
                .canNang(entity.getCanNang())
                .nhietDo(entity.getNhietDo())
                .ketQua(entity.getKetQua())
                .lyDoTuChoi(entity.getLyDoTuChoi())
                .build();
    }
}
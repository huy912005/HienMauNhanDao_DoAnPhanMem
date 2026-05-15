package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.response.TuiMauResponse;
import com.Nhom20.DoAnPhamMem.entity.TuiMauEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TuiMauMapper {

    @Mapping(target = "maDon", source = "donDangKy.maDon")
    @Mapping(target = "tenTinhNguyenVien", source = "donDangKy.tinhNguyenVien.hoTen")
    @Mapping(target = "nhomMau", expression = "java(entity.getDonDangKy() != null && entity.getDonDangKy().getTinhNguyenVien() != null && entity.getDonDangKy().getTinhNguyenVien().getNhomMau() != null ? entity.getDonDangKy().getTinhNguyenVien().getNhomMau().getDbValue() : null)")
    @Mapping(target = "theTich", source = "theTich.ml")
    @Mapping(target = "trangThai", source = "trangThai.dbValue")
    @Mapping(target = "tenChienDich", source = "donDangKy.chienDich.tenChienDich")
    @Mapping(target = "tenBacSi", expression = "java(entity.getDonDangKy().getKetQuaLamSangList() != null && !entity.getDonDangKy().getKetQuaLamSangList().isEmpty() ? entity.getDonDangKy().getKetQuaLamSangList().get(0).getBacSiKham().getHoTen() : null)")
    @Mapping(target = "maBacSi", expression = "java(entity.getDonDangKy().getKetQuaLamSangList() != null && !entity.getDonDangKy().getKetQuaLamSangList().isEmpty() ? entity.getDonDangKy().getKetQuaLamSangList().get(0).getBacSiKham().getMaNhanVien() : null)")
    TuiMauResponse toResponse(TuiMauEntity entity);

    @Mapping(target = "nhomMau", source = "khoMau.nhomMau.dbValue")
    @Mapping(target = "ngayThuNhan", source = "thoiGianLayMau")
    @Mapping(target = "theTich", source = "theTich.ml")
    @Mapping(target = "nhietDoVanChuyen", source = "nhietDoVanChuyen")
    @Mapping(target = "trangThai", source = "trangThai.dbValue")
    @Mapping(target = "ngayHetHan", ignore = true) // Sẽ tính toán trong Service
    @Mapping(target = "tinhTrangHSD", ignore = true) // Sẽ tính toán trong Service
    com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO toBloodUnitDTO(TuiMauEntity entity);
}

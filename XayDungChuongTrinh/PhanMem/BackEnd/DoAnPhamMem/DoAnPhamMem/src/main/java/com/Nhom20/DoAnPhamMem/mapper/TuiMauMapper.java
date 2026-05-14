package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.response.TuiMauResponse;
import com.Nhom20.DoAnPhamMem.entity.TuiMauEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TuiMauMapper {

    @Mapping(target = "maDon", source = "donDangKy.maDon")
    @Mapping(target = "tenTinhNguyenVien", source = "donDangKy.tinhNguyenVien.hoTen")
    @Mapping(target = "nhomMau", source = "donDangKy.tinhNguyenVien.nhomMau.dbValue")
    @Mapping(target = "theTich", source = "theTich.ml")
    @Mapping(target = "trangThai", source = "trangThai.dbValue")
    @Mapping(target = "tenChienDich", source = "donDangKy.chienDich.tenChienDich")
    TuiMauResponse toResponse(TuiMauEntity entity);

    @Mapping(target = "nhomMau", source = "khoMau.nhomMau.dbValue")
    @Mapping(target = "ngayThuNhan", source = "thoiGianLayMau")
    @Mapping(target = "theTich", source = "theTich.ml")
    @Mapping(target = "trangThai", source = "trangThai.dbValue")
    @Mapping(target = "ngayHetHan", ignore = true) // Sẽ tính toán trong Service
    @Mapping(target = "tinhTrangHSD", ignore = true) // Sẽ tính toán trong Service
    com.Nhom20.DoAnPhamMem.dto.response.BloodUnitDTO toBloodUnitDTO(TuiMauEntity entity);
}

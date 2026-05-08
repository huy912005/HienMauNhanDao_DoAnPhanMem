package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.response.KetQuaLamSangResponse;
import com.Nhom20.DoAnPhamMem.entity.KetQuaLamSangEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface KetQuaLamSangMapper {

    @Mapping(target = "maDon", source = "donDangKy.maDon")
    @Mapping(target = "tenTinhNguyenVien", source = "donDangKy.tinhNguyenVien.hoTen")
    @Mapping(target = "tenChienDich", source = "donDangKy.chienDich.tenChienDich")
    @Mapping(target = "tenBacSi", source = "bacSiKham.hoTen")
    KetQuaLamSangResponse toResponse(KetQuaLamSangEntity entity);
}

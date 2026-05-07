package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.response.NhanVienResponse;
import com.Nhom20.DoAnPhamMem.entity.NhanVienEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NhanVienMapper {

    @Mapping(target = "maNV", source = "maNhanVien")
    @Mapping(target = "hoVaTen", source = "hoTen")
    @Mapping(target = "maKhoa", source = "khoaCongTac.maKhoa")
    @Mapping(target = "cccd", source = "cccd")
    @Mapping(target = "gioiTinh", source = "gioiTinh")
    @Mapping(target = "soDienThoai", source = "soDienThoai")
    NhanVienResponse toResponse(NhanVienEntity entity);
}

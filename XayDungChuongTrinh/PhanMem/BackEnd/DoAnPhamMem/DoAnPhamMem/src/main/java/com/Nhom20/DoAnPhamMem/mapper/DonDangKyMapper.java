package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.response.DonDangKyResponse;
import com.Nhom20.DoAnPhamMem.dto.request.DonDangKyRequest;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DonDangKyMapper {

    @Mapping(target = "maDon", ignore = true)
    @Mapping(target = "maQR", ignore = true)
    @Mapping(target = "tinhNguyenVien", ignore = true)
    @Mapping(target = "chienDich", ignore = true)
    @Mapping(target = "nhanVienPhuTrach", ignore = true)
    @Mapping(target = "thoiGianDangKy", ignore = true)
    @Mapping(target = "trangThai", ignore = true)
    @Mapping(target = "theTich", ignore = true)
    DonDangKyEntity toEntity(DonDangKyRequest request);

    @Mapping(target = "maTNV", source = "tinhNguyenVien.maTNV")
    @Mapping(target = "maChienDich", source = "chienDich.maChienDich")
    @Mapping(target = "maNhanVien", source = "nhanVienPhuTrach.maNhanVien")
    @Mapping(target = "trangThai", source = "trangThai.dbValue")
    DonDangKyResponse toResponse(DonDangKyEntity entity);

    @Named("stringToEnum")
    default TrangThaiDonDangKy stringToEnum(String value) {
        if (value == null || value.isEmpty()) {
            return TrangThaiDonDangKy.DA_DANG_KY;
        }
        return TrangThaiDonDangKy.fromDbValue(value);
    }
}
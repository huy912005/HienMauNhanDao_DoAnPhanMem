package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.response.DonDangKyResponse;
import com.Nhom20.DoAnPhamMem.dto.request.DonDangKyRequest;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.entity.ChienDichHienMauEntity;
import com.Nhom20.DoAnPhamMem.dto.response.ChienDichDonDangKyResponse;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { TinhNguyenVienMapper.class })
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

    @Mapping(target = "tinhNguyenVien", source = "tinhNguyenVien")
    @Mapping(target = "maTNV", source = "tinhNguyenVien.maTNV")
    @Mapping(target = "maChienDich", source = "chienDich.maChienDich")
    @Mapping(target = "maNV", source = "nhanVienPhuTrach.maNhanVien")
    @Mapping(target = "trangThai", source = "trangThai.dbValue")
    @Mapping(target = "theTich", source = "theTich.value")
    @Mapping(target = "chienDich", source = "chienDich")
    DonDangKyResponse toResponse(DonDangKyEntity entity);

    @Mapping(target = "tenChienDich", source = "tenChienDich")
    @Mapping(target = "thoiGianBD", source = "thoiGianBD")
    @Mapping(target = "thoiGianKT", source = "thoiGianKT")
    @Mapping(target = "maChienDich", source = "maChienDich")
    @Mapping(target = "diaDiem", source = "diaDiem")
    ChienDichDonDangKyResponse toChienDichResponse(ChienDichHienMauEntity entity);

    @Named("stringToEnum")
    default TrangThaiDonDangKy stringToEnum(String value) {
        if (value == null || value.isEmpty()) {
            return TrangThaiDonDangKy.DA_DANG_KY;
        }
        return TrangThaiDonDangKy.fromDbValue(value);
    }
}

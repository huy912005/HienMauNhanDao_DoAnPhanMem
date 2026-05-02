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
    DonDangKyEntity toEntity(DonDangKyRequest request);

    @Mapping(target = "trangThai", source = "trangThai.dbValue")
    DonDangKyResponse toResponse(DonDangKyEntity entity);

    // Xử lý logic convert String sang Enum
    @Named("stringToEnum")
    default TrangThaiDonDangKy stringToEnum(String value) {
        if (value == null || value.isEmpty()) {
            return TrangThaiDonDangKy.DA_DANG_KY; // Giá trị mặc định
        }
        return TrangThaiDonDangKy.fromDbValue(value);
    }
}
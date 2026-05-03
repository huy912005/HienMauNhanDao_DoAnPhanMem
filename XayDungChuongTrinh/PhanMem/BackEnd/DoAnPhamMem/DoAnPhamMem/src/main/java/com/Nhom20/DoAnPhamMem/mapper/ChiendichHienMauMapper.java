package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.request.ChiendichHienMauRequest;
import com.Nhom20.DoAnPhamMem.dto.response.ChiendichHienMauResponse;
import com.Nhom20.DoAnPhamMem.entity.ChienDichHienMauEntity;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiChienDich;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChiendichHienMauMapper {
    @Mapping(target = "trangThai", qualifiedByName = "stringToEnum")
    ChienDichHienMauEntity toEntity(ChiendichHienMauRequest request);
    @Mapping(target = "trangThai", source = "trangThai.dbValue")
    ChiendichHienMauResponse toResponse(ChienDichHienMauEntity entity);
    List<ChiendichHienMauResponse> toResponseList(List<ChienDichHienMauEntity> entities);

    // Logic hỗ trợ convert String sang Enum
    @Named("stringToEnum")
    default TrangThaiChienDich stringToEnum(String value) {
        if (value == null || value.isEmpty()) {
            return TrangThaiChienDich.DANG_LAP_KE_HOACH;
        }
        return TrangThaiChienDich.fromDbValue(value);
    }
}
package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.request.HoSoSucKhoeRequest;
import com.Nhom20.DoAnPhamMem.dto.response.HoSoSucKhoeResponse;
import com.Nhom20.DoAnPhamMem.entity.HoSoSucKhoeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HoSoSucKhoeMapper {
    @Mapping(target = "maHoSo", ignore = true)
    @Mapping(target = "donDangKy", ignore = true)
    HoSoSucKhoeEntity toEntity(HoSoSucKhoeRequest request);
    @Mapping(target = "maDon", source = "donDangKy.maDon")
    HoSoSucKhoeResponse toResponse(HoSoSucKhoeEntity entity);
    List<HoSoSucKhoeResponse> toResponseList(List<HoSoSucKhoeEntity> list);
}

package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.request.HoSoSucKhoeRequest;
import com.Nhom20.DoAnPhamMem.dto.response.HoSoSucKhoeResponse;
import com.Nhom20.DoAnPhamMem.entity.HoSoSucKhoeEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HoSoSucKhoeMapper {
    HoSoSucKhoeEntity toEntity(HoSoSucKhoeRequest hoSoSucKhoeRequest);
    HoSoSucKhoeResponse toResponse(HoSoSucKhoeEntity hoSoSucKhoeEntity);
    List<HoSoSucKhoeResponse> toResponseList(List<HoSoSucKhoeEntity> hoSoSucKhoeEntityList);
}

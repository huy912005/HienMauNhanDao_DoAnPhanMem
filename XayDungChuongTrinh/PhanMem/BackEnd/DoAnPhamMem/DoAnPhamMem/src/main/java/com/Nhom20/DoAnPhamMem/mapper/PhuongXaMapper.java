package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.request.PhuongXaRequest;
import com.Nhom20.DoAnPhamMem.dto.response.PhuongXaResponse;
import com.Nhom20.DoAnPhamMem.entity.PhuongXaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhuongXaMapper {
    @Mapping(target = "maPhuongXa", ignore = true)
    PhuongXaEntity toEntity(PhuongXaRequest request);
    PhuongXaResponse toResponse(PhuongXaEntity entity);
    List<PhuongXaResponse> toResponseList(List<PhuongXaEntity> entities);
}

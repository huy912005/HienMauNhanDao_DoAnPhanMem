package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.request.DiaDiemRequest;
import com.Nhom20.DoAnPhamMem.dto.response.DiaDiemResponse;
import com.Nhom20.DoAnPhamMem.entity.DiaDiemEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiaDiemMapper {
    DiaDiemEntity  dtoToEntity(DiaDiemRequest diaDiemRequest);
    DiaDiemResponse dtoToDto(DiaDiemEntity diaDiemEntity);
    List<DiaDiemResponse> toResponseList(List<DiaDiemEntity> diaDiemEntityList);
}

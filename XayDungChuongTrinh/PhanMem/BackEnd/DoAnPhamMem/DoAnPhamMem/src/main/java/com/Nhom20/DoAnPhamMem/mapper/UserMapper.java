package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.entity.TaiKhoanEntity;
import com.Nhom20.DoAnPhamMem.dto.request.UserRequest;
import com.Nhom20.DoAnPhamMem.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    TaiKhoanEntity toEntity(UserRequest request);

    @Mapping(source = "vaiTro.tenVaiTro", target = "vaiTro")
    UserResponse toResponse(TaiKhoanEntity entity);
}

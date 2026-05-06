package com.Nhom20.DoAnPhamMem.mapper;

import com.Nhom20.DoAnPhamMem.dto.request.TinhNguyenVienRequest;
import com.Nhom20.DoAnPhamMem.dto.response.TinhNguyenVienReSponse;
import com.Nhom20.DoAnPhamMem.entity.TinhNguyenVienEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TinhNguyenVienMapper {

    // toEntity: mapper tự map tất cả field cùng tên (hoTen, cccd, ngaySinh,
    // gioiTinh,
    // soDienThoai, diaChi, nhomMau). Ignore mã PK và khóa ngoại vì service tự set.
    @Mapping(target = "maTNV", ignore = true)
    @Mapping(target = "taiKhoan", ignore = true)
    @Mapping(target = "phuongXa", ignore = true)
    @Mapping(target = "hoTen", source = "hoVaTen")
    @Mapping(target = "cccd", source = "soCCCD")
    TinhNguyenVienEntity toEntity(TinhNguyenVienRequest request);

    // updateEntityFromRequest: dùng khi UPDATE - cập nhật entity đang có
    // Không chạm vào maTNV, taiKhoan (bất biến sau khi tạo), phuongXa (service set
    // riêng)
    @Mapping(target = "maTNV", ignore = true)
    @Mapping(target = "taiKhoan", ignore = true)
    @Mapping(target = "phuongXa", ignore = true)
    @Mapping(target = "hoTen", source = "hoVaTen")
    @Mapping(target = "cccd", source = "soCCCD")
    void updateEntityFromRequest(TinhNguyenVienRequest request, @MappingTarget TinhNguyenVienEntity entity);

    // toResponse: map entity sang DTO response
    @Mapping(target = "hoVaTen", source = "hoTen")
    @Mapping(target = "soCCCD", source = "cccd")
    @Mapping(target = "maTaiKhoan", source = "taiKhoan.maTaiKhoan")
    @Mapping(target = "email", source = "taiKhoan.email")
    @Mapping(target = "trangThai", source = "taiKhoan.trangThai")
    @Mapping(target = "maPhuongXa", source = "phuongXa.maPhuongXa")
    TinhNguyenVienReSponse toResponse(TinhNguyenVienEntity entity);

    List<TinhNguyenVienReSponse> toResponseList(List<TinhNguyenVienEntity> entities);
}

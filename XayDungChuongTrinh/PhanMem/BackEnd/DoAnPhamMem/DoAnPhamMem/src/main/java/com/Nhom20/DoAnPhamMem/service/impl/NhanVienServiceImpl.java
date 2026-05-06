package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.response.NhanVienResponse;
import com.Nhom20.DoAnPhamMem.entity.NhanVienEntity;
import com.Nhom20.DoAnPhamMem.mapper.NhanVienMapper;
import com.Nhom20.DoAnPhamMem.repository.NhanVienRepository;
import com.Nhom20.DoAnPhamMem.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NhanVienServiceImpl implements NhanVienService {

    private final NhanVienRepository nhanVienRepository;
    private final NhanVienMapper nhanVienMapper;

    @Override
    public ApiResponse<NhanVienResponse> getByMaTaiKhoan(String maTaiKhoan) {
        Optional<NhanVienEntity> nhanVien = nhanVienRepository.findByTaiKhoan_MaTaiKhoan(maTaiKhoan);
        if (nhanVien.isEmpty()) {
            nhanVien = nhanVienRepository.findByTaiKhoan_Email(maTaiKhoan);
        }
        if (nhanVien.isEmpty()) {
            return ApiResponse.<NhanVienResponse>builder()
                    .status(false)
                    .message("Không tìm thấy nhân viên")
                    .build();
        }
        return ApiResponse.<NhanVienResponse>builder()
                .status(true)
                .message("Lấy thông tin thành công")
                .data(nhanVienMapper.toResponse(nhanVien.get()))
                .build();
    }
}

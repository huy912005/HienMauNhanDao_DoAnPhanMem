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
        String key = maTaiKhoan != null ? maTaiKhoan.trim() : "";
        System.out.println("DEBUG: Dang tim nhan vien cho tai khoan/email: [" + key + "]");
        
        Optional<NhanVienEntity> nhanVien = key.isEmpty() ? Optional.empty() : nhanVienRepository.findByAccount(key);
        
        if (nhanVien.isEmpty()) {
            System.out.println("DEBUG: KHONG tim thay nhan vien trong DB!");
            return ApiResponse.<NhanVienResponse>builder()
                    .status(false)
                    .message("Không tìm thấy nhân viên liên kết với tài khoản này")
                    .build();
        }

        NhanVienResponse response = nhanVienMapper.toResponse(nhanVien.get());
        System.out.println("DEBUG: Da tim thay nhan vien: " + response.getMaNV() + " - " + response.getHoVaTen());
        
        return ApiResponse.<NhanVienResponse>builder()
                .status(true)
                .message("Lấy thông tin nhân viên thành công")
                .data(response)
                .build();
    }
}

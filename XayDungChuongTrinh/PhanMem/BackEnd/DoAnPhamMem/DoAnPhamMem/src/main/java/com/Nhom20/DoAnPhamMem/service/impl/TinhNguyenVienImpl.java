package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.TinhNguyenVienRequest;
import com.Nhom20.DoAnPhamMem.dto.response.TinhNguyenVienReSponse;
import com.Nhom20.DoAnPhamMem.entity.PhuongXaEntity;
import com.Nhom20.DoAnPhamMem.entity.TaiKhoanEntity;
import com.Nhom20.DoAnPhamMem.entity.TinhNguyenVienEntity;
import com.Nhom20.DoAnPhamMem.mapper.TinhNguyenVienMapper;
import com.Nhom20.DoAnPhamMem.repository.PhuongXaRepository;
import com.Nhom20.DoAnPhamMem.repository.TaiKhoanRepository;
import com.Nhom20.DoAnPhamMem.repository.TinhNguyenVienRepository;
import com.Nhom20.DoAnPhamMem.service.TinhNguyenVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TinhNguyenVienImpl implements TinhNguyenVienService {

    private final TinhNguyenVienRepository tinhNguyenVienRepository;
    private final TinhNguyenVienMapper tinhNguyenVienMapper;
    private final TaiKhoanRepository taiKhoanRepository;
    private final PhuongXaRepository phuongXaRepository;
    private TaiKhoanEntity resolveTaiKhoan(String maTaiKhoanOrEmail) {
        if (maTaiKhoanOrEmail == null || maTaiKhoanOrEmail.isBlank())
            return null;
        return taiKhoanRepository.findById(maTaiKhoanOrEmail).orElseGet(() -> taiKhoanRepository.findByEmail(maTaiKhoanOrEmail).orElse(null));
    }
    @Override
    public ApiResponse<TinhNguyenVienReSponse> createTinhNguyenVien(TinhNguyenVienRequest request) {
        TinhNguyenVienEntity entity = tinhNguyenVienMapper.toEntity(request);
        Integer max = tinhNguyenVienRepository.findMaxMaTNV();
        entity.setMaTNV(String.format("TN%05d", (max == null) ? 1 : max + 1));
        entity.setTaiKhoan(resolveTaiKhoan(request.getMaTaiKhoan()!=null?request.getMaTaiKhoan():null));
        entity.setPhuongXa(phuongXaRepository.findById(request.getMaPhuongXa()).orElse(null));
        tinhNguyenVienRepository.save(entity);
        return ApiResponse.<TinhNguyenVienReSponse>builder().status(true).message("Tạo mới tình nguyện viên thành công!").data(tinhNguyenVienMapper.toResponse(entity)).build();
    }
    @Override
    public ApiResponse<TinhNguyenVienReSponse> createOrUpdateTinhNguyenVien(TinhNguyenVienRequest request) {
        TaiKhoanEntity taiKhoan = resolveTaiKhoan(request.getMaTaiKhoan());
        if (taiKhoan == null)
            return ApiResponse.<TinhNguyenVienReSponse>builder().status(false).message("Không tìm thấy tài khoản: " + request.getMaTaiKhoan()).data(null).build();
        Optional<TinhNguyenVienEntity> existing = tinhNguyenVienRepository.findByTaiKhoan_MaTaiKhoan(taiKhoan.getMaTaiKhoan());
        TinhNguyenVienEntity entity;
        String message;
        if (existing.isPresent()) {
            // --- UPDATE: mapper cập nhật các field thông thường từ request vào entity đã có ---
            entity = existing.get();
            tinhNguyenVienMapper.updateEntityFromRequest(request, entity);
            // Cập nhật khóa ngoại phuongXa nếu có thay đổi
            entity.setPhuongXa(phuongXaRepository.findById(request.getMaPhuongXa()).orElse(null));
            message = "Cập nhật thông tin tình nguyện viên thành công!";
        } else {
            entity = tinhNguyenVienMapper.toEntity(request);
            Integer max = tinhNguyenVienRepository.findMaxMaTNV();
            entity.setMaTNV(String.format("TN%05d", (max == null) ? 1 : max + 1));
            entity.setTaiKhoan(taiKhoan);
            entity.setPhuongXa(phuongXaRepository.findById(request.getMaPhuongXa()).orElse(null));
            message = "Tạo mới tình nguyện viên thành công!";
        }
        tinhNguyenVienRepository.save(entity);
        return ApiResponse.<TinhNguyenVienReSponse>builder().status(true).message(message).data(tinhNguyenVienMapper.toResponse(entity)).build();
    }
    @Override
    public ApiResponse<TinhNguyenVienReSponse> getByMaTaiKhoan(String maTaiKhoanOrEmail) {
        TaiKhoanEntity taiKhoan = resolveTaiKhoan(maTaiKhoanOrEmail);
        if (taiKhoan == null)
            return ApiResponse.<TinhNguyenVienReSponse>builder().status(false).message("Không tìm thấy tài khoản.").data(null).build();
        return tinhNguyenVienRepository.findByTaiKhoan_MaTaiKhoan(taiKhoan.getMaTaiKhoan())
                .map(entity -> ApiResponse.<TinhNguyenVienReSponse>builder().status(true).message("Lấy thông tin tình nguyện viên thành công!").data(tinhNguyenVienMapper.toResponse(entity)).build())
                .orElse(ApiResponse.<TinhNguyenVienReSponse>builder().status(false).message("Chưa có thông tin tình nguyện viên với tài khoản này.").data(null).build());
    }
}

package com.Nhom20.DoAnPhamMem.service;

import com.Nhom20.DoAnPhamMem.dto.request.KetQuaXetNghiemRequest;
import com.Nhom20.DoAnPhamMem.dto.response.KetQuaXetNghiemResponse;

import java.util.List;
import java.util.Optional;

public interface KetQuaXetNghiemService {
    // Lấy toàn bộ danh sách kết quả xét nghiệm
    List<KetQuaXetNghiemResponse> getAll();

    // Tạo kết quả xét nghiệm mới (mã tự sinh theo format XN00001+)
    KetQuaXetNghiemResponse create(KetQuaXetNghiemRequest request);

    // Lấy kết quả xét nghiệm theo mã túi máu
    Optional<KetQuaXetNghiemResponse> getByMaTuiMau(String maTuiMau);

    // Cập nhật kết quả (bác sĩ nhập nhóm máu, số lần, mô tả, kết quả)
    KetQuaXetNghiemResponse update(String maKQ, KetQuaXetNghiemRequest request);
}


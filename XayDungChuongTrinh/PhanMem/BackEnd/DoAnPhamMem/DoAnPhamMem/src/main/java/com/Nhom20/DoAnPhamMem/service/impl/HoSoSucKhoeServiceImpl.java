package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.common.ApiResponse;
import com.Nhom20.DoAnPhamMem.dto.request.HoSoSucKhoeRequest;
import com.Nhom20.DoAnPhamMem.dto.response.HoSoSucKhoeResponse;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.entity.HoSoSucKhoeEntity;
import com.Nhom20.DoAnPhamMem.mapper.HoSoSucKhoeMapper;
import com.Nhom20.DoAnPhamMem.repository.DonDangKyRepository;
import com.Nhom20.DoAnPhamMem.repository.HoSoSucKhoeRepository;
import com.Nhom20.DoAnPhamMem.service.HoSoSucKhoeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoSoSucKhoeServiceImpl implements HoSoSucKhoeService {

    private final HoSoSucKhoeRepository hoSoSucKhoeRepository;
    private final HoSoSucKhoeMapper hoSoSucKhoeMapper;
    private final DonDangKyRepository donDangKyRepository;

    @Override
    public ApiResponse<List<HoSoSucKhoeResponse>> getAll() {
        return ApiResponse.<List<HoSoSucKhoeResponse>>builder()
                .status(true)
                .message("Lấy danh sách thành công!")
                .data(hoSoSucKhoeMapper.toResponseList(hoSoSucKhoeRepository.findAll()))
                .build();
    }

    @Override
    public ApiResponse<HoSoSucKhoeResponse> createHoSo(HoSoSucKhoeRequest request) {
        // Mapper map các field thông thường: khangSinh, truyenNhiem, dauHong, coThai, moTaKhac
        // Service set: mã PK (maHoSo) và khóa ngoại entity (donDangKy)
        HoSoSucKhoeEntity entity = hoSoSucKhoeMapper.toEntity(request);

        // Set mã PK - format HS + 5 số = 7 ký tự (khớp CHAR(7))
        Integer max = hoSoSucKhoeRepository.findMaxMaHoSo();
        entity.setMaHoSo(String.format("HS%05d", (max == null) ? 1 : max + 1));

        // Set khóa ngoại donDangKy (mapper đã ignore field này)
        DonDangKyEntity donDangKy = donDangKyRepository.findById(request.getMaDon())
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy đơn đăng ký với maDon: " + request.getMaDon()));
        entity.setDonDangKy(donDangKy);

        hoSoSucKhoeRepository.save(entity);

        return ApiResponse.<HoSoSucKhoeResponse>builder()
                .status(true)
                .message("Tạo hồ sơ khai báo sức khỏe thành công!")
                .data(hoSoSucKhoeMapper.toResponse(entity))
                .build();
    }

    @Override
    public ApiResponse<HoSoSucKhoeResponse> getHoSoByMaDon(String maDon) {
        return hoSoSucKhoeRepository.findByDonDangKy_MaDon(maDon)
                .map(entity -> ApiResponse.<HoSoSucKhoeResponse>builder()
                        .status(true)
                        .message("Lấy hồ sơ sức khỏe thành công!")
                        .data(hoSoSucKhoeMapper.toResponse(entity))
                        .build())
                .orElse(ApiResponse.<HoSoSucKhoeResponse>builder()
                        .status(false)
                        .message("Chưa có hồ sơ sức khỏe cho đơn này.")
                        .data(null)
                        .build());
    }
}

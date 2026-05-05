package com.Nhom20.DoAnPhamMem.service.impl;

import com.Nhom20.DoAnPhamMem.dto.request.KhamSangLocRequest;
import com.Nhom20.DoAnPhamMem.service.KhamSangLocService;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.entity.KetQuaLamSangEntity;
import com.Nhom20.DoAnPhamMem.entity.NhanVienEntity;
import com.Nhom20.DoAnPhamMem.entity.TuiMauEntity;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau;
import com.Nhom20.DoAnPhamMem.repository.DonDangKyRepository;
import com.Nhom20.DoAnPhamMem.repository.KetQuaLamSangRepository;
import com.Nhom20.DoAnPhamMem.repository.NhanVienRepository;
import com.Nhom20.DoAnPhamMem.repository.TuiMauRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KhamSangLocServiceImpl implements KhamSangLocService {

    private final DonDangKyRepository donDangKyRepository;
    private final KetQuaLamSangRepository ketQuaLamSangRepository;
    private final TuiMauRepository tuiMauRepository;
    private final NhanVienRepository nhanVienRepository;

    @Override
    @Transactional
    public void xuLyKetQuaSangLoc(KhamSangLocRequest request) {
        log.info("Bắt đầu xử lý khám sàng lọc cho đơn: {}", request.getMaDon());

        // 1. Kiểm tra đơn đăng ký tồn tại
        DonDangKyEntity donDangKy = donDangKyRepository.findById(request.getMaDon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đăng ký với mã: " + request.getMaDon()));

        // Lấy thông tin nhân viên thực hiện thu nhận
        NhanVienEntity nhanVien = nhanVienRepository.findById(request.getMaNhanVien())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với mã: " + request.getMaNhanVien()));

        // 2. Lưu kết quả khám lâm sàng
        boolean isDat = "Đạt yêu cầu".equalsIgnoreCase(request.getKetQua());
        
        KetQuaLamSangEntity kqLamSang = new KetQuaLamSangEntity();
        kqLamSang.setMaKQ(generateId("KL")); // Sinh mã ngẫu nhiên cho KQ Lâm Sàng
        kqLamSang.setDonDangKy(donDangKy);
        kqLamSang.setNhanVien(nhanVien);
        
        kqLamSang.setHuyetAp(request.getHuyetAp());
        kqLamSang.setNhipTim(request.getNhipTim());
        kqLamSang.setCanNang(request.getCanNang());
        kqLamSang.setKetQua(isDat);
        
        if (!isDat) {
            kqLamSang.setLyDoTuChoi("Chỉ số không đạt tiêu chuẩn qua khám lâm sàng");
        }
        
        // Khi save, Trigger CSDL sẽ tự động đổi DONDANGKY thành 'Không đạt' nếu ketQua = false
        ketQuaLamSangRepository.save(kqLamSang);

        // 3. Nếu đạt, tiến hành tạo túi máu
        if (isDat) {
            log.info("Đơn {} đạt chuẩn, tiến hành tạo túi máu.", request.getMaDon());
            
            TuiMauEntity tuiMau = new TuiMauEntity();
            tuiMau.setMaTuiMau(generateId("TM")); // Sinh mã tự động CHAR(7)
            tuiMau.setDonDangKy(donDangKy);
            tuiMau.setNhanVien(nhanVien);
            
            tuiMau.setMaVach(request.getMaVachTuiMau()); // Mã Barcode thực tế quét được dán trên túi
            tuiMau.setTheTich(request.getTheTichHien());
            tuiMau.setThoiGianLayMau(LocalDateTime.now());
            tuiMau.setTrangThai(TrangThaiTuiMau.CHO_XET_NGHIEM);
            
            // Khi save, Trigger CSDL sẽ tự động đổi DONDANGKY thành 'Đã hiến' và gán theTich 
            tuiMauRepository.save(tuiMau);
            
            log.info("Đã thu nhận túi máu thành công với mã vạch: {}", tuiMau.getMaVach());
        }
    }

    // Hàm hỗ trợ sinh mã CHAR(7) khớp định dạng CSDL (VD: KL0ABCD, TM0XYZ9)
    private String generateId(String prefix) {
        return prefix + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
}
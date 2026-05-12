package com.Nhom20.DoAnPhamMem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;

@Repository
public interface DonDangKyRepository extends JpaRepository<DonDangKyEntity, String> {
    List<DonDangKyEntity> findByTrangThaiIn(List<TrangThaiDonDangKy> trangThai);

    // maDon có format "DK00001" => prefix 2 ký tự "DK", dùng SUBSTRING từ vị trí 3
    @Query(value = "SELECT MAX(CAST(SUBSTRING(maDon, 3) AS UNSIGNED)) FROM dondangky", nativeQuery = true)
    Integer findMaxMaDon();

    // Kiểm tra TNV đã đăng ký chiến dịch chưa
    Optional<DonDangKyEntity> findByTinhNguyenVien_MaTNVAndChienDich_MaChienDich(String maTNV, String maChienDich);

    // Lấy danh sách đơn đăng ký của một tình nguyện viên
    Page<DonDangKyEntity> findByTinhNguyenVien_MaTNV(String maTNV, Pageable pageable);

    // Lấy danh sách chờ thu nhận máu (Chuẩn: Khám đạt, Chưa hiến, Chưa có túi máu, không lặp lại)
    @Query(value = "SELECT DISTINCT d.* FROM dondangky d INNER JOIN ketqualamsang k ON d.maDon = k.maDon " +
                   "WHERE k.ketQua = 1 AND d.trangThai <> 'Đã hiến' " +
                   "AND d.maDon NOT IN (SELECT maDon FROM tuimau)", 
           countQuery = "SELECT count(DISTINCT d.maDon) FROM dondangky d INNER JOIN ketqualamsang k ON d.maDon = k.maDon " +
                        "WHERE k.ketQua = 1 AND d.trangThai <> 'Đã hiến' " +
                        "AND d.maDon NOT IN (SELECT maDon FROM tuimau)",
           nativeQuery = true)
    Page<DonDangKyEntity> findReadyForCollection(Pageable pageable);
}

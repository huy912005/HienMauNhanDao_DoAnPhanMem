package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
}

package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.TinhNguyenVienEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TinhNguyenVienRepository extends JpaRepository<TinhNguyenVienEntity, String> {
    // maTNV có format "TN00001" => prefix 2 ký tự "TN", dùng SUBSTRING từ vị trí 3
    @Query(value = "SELECT MAX(CAST(SUBSTRING(maTNV, 3) AS UNSIGNED)) FROM tinhnguyenvien", nativeQuery = true)
    Integer findMaxMaTNV();

    // Tìm TNV theo maTaiKhoan (email được dùng làm maTaiKhoan từ frontend)
    Optional<TinhNguyenVienEntity> findByTaiKhoan_MaTaiKhoan(String maTaiKhoan);
}

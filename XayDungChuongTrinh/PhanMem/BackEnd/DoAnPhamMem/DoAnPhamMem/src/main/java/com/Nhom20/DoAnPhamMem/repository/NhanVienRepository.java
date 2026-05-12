package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.NhanVienEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVienEntity, String> {
    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM NHANVIEN WHERE maTaiKhoan LIKE CONCAT('%', :id, '%') OR maTaiKhoan IN (SELECT maTaiKhoan FROM TAIKHOAN WHERE email = :id)", nativeQuery = true)
    java.util.Optional<NhanVienEntity> findByAccount(@org.springframework.data.repository.query.Param("id") String id);

    java.util.Optional<NhanVienEntity> findByTaiKhoan_MaTaiKhoan(String maTaiKhoan);
    java.util.Optional<NhanVienEntity> findByTaiKhoan_Email(String email);
}

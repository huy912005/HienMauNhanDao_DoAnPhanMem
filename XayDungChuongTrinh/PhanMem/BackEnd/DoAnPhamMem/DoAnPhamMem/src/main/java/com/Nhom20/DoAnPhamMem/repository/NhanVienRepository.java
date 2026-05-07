package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.NhanVienEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVienEntity, String> {
    java.util.Optional<NhanVienEntity> findByTaiKhoan_MaTaiKhoan(String maTaiKhoan);
    java.util.Optional<NhanVienEntity> findByTaiKhoan_Email(String email);
}

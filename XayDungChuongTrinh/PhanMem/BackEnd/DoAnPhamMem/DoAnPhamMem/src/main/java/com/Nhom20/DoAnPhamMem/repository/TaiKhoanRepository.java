package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.TaiKhoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoanEntity, String> {
    @org.springframework.data.jpa.repository.Query("SELECT t FROM TaiKhoanEntity t JOIN FETCH t.vaiTro WHERE t.email = :email")
    Optional<TaiKhoanEntity> findByEmail(@org.springframework.data.repository.query.Param("email") String email);
}

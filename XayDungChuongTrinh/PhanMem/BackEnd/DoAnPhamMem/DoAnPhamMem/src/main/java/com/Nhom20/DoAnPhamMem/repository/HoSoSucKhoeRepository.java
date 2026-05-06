package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.HoSoSucKhoeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HoSoSucKhoeRepository extends JpaRepository<HoSoSucKhoeEntity,String> {
    @Query(value = "SELECT MAX(CAST(SUBSTRING(maHoSo, 3) AS UNSIGNED)) FROM hososuckhoe", nativeQuery = true)
    Integer findMaxMaHoSo();

    java.util.Optional<HoSoSucKhoeEntity> findByDonDangKy_MaDon(String maDon);
}

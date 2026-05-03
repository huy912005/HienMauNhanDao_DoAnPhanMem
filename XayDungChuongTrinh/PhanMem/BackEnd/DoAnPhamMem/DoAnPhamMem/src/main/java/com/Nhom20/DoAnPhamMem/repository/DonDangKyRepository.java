package com.Nhom20.DoAnPhamMem.repository;
import com.Nhom20.DoAnPhamMem.entity.DonDangKyEntity;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonDangKyRepository extends JpaRepository<DonDangKyEntity, String> {
    List<DonDangKyEntity> findByTrangThai(TrangThaiDonDangKy trangThai);
}

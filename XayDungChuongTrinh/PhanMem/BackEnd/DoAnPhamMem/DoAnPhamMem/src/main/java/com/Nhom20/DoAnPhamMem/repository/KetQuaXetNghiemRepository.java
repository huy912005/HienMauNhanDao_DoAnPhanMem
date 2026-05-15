package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.KetQuaXetNghiemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KetQuaXetNghiemRepository extends JpaRepository<KetQuaXetNghiemEntity, String> {

    Optional<KetQuaXetNghiemEntity> findByTuiMau_MaTuiMau(String maTuiMau);

    @Query(value = "SELECT MAX(CAST(SUBSTRING(maKQ, 3) AS UNSIGNED)) FROM KETQUAXETNGHIEM", nativeQuery = true)
    Integer findMaxMaKQ();
}

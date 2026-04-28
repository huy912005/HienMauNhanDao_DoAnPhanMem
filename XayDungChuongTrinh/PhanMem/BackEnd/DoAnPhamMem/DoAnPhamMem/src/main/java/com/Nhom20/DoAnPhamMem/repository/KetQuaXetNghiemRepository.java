package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.KetQuaXetNghiemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KetQuaXetNghiemRepository extends JpaRepository<KetQuaXetNghiemEntity,String> {
}

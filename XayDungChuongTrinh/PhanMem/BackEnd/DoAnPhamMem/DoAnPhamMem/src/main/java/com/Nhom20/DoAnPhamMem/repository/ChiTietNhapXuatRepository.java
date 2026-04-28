package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.ChiTietNhapXuatEntity;
import com.Nhom20.DoAnPhamMem.entity.ChiTietNhapXuatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietNhapXuatRepository extends JpaRepository<ChiTietNhapXuatEntity, ChiTietNhapXuatId> {
}

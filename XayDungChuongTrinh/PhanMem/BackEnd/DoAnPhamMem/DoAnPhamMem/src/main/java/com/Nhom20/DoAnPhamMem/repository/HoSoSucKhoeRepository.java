package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.HoSoSucKhoeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoSoSucKhoeRepository extends JpaRepository<HoSoSucKhoeEntity,String> {
}

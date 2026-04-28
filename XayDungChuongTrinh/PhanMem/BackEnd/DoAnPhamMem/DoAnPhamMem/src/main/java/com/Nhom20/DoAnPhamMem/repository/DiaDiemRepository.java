package com.Nhom20.DoAnPhamMem.repository;
import com.Nhom20.DoAnPhamMem.entity.DiaDiemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaDiemRepository extends JpaRepository<DiaDiemEntity,String> {
}

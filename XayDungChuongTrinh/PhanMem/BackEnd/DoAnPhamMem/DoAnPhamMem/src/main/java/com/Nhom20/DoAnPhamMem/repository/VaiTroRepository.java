package com.Nhom20.DoAnPhamMem.repository;
import com.Nhom20.DoAnPhamMem.entity.VaiTroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTroEntity, String> {}

package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.InvalidatedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedTokenEntity, String> {
}

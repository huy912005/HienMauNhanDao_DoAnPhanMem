package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.KhoMauEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoMauRepository extends JpaRepository<KhoMauEntity,String> {

    @org.springframework.data.jpa.repository.Query("SELECT new com.Nhom20.DoAnPhamMem.dto.response.BloodTypeStatDTO(CAST(k.nhomMau AS string), k.soLuongTon) FROM KhoMauEntity k")
    java.util.List<com.Nhom20.DoAnPhamMem.dto.response.BloodTypeStatDTO> getInventoryByBloodType();

}

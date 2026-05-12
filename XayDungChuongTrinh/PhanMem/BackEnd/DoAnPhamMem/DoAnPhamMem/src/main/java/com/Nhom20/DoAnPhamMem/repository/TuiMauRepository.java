package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.TuiMauEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuiMauRepository extends JpaRepository<TuiMauEntity, String> {
        long countByTrangThai(com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau trangThai);

        @org.springframework.data.jpa.repository.Query("SELECT new com.Nhom20.DoAnPhamMem.dto.response.MonthlyCollectionStatDTO(MONTH(t.thoiGianLayMau), CAST(COUNT(t) AS int)) "
                        +
                        "FROM TuiMauEntity t WHERE YEAR(t.thoiGianLayMau) = :year GROUP BY MONTH(t.thoiGianLayMau)")
        java.util.List<com.Nhom20.DoAnPhamMem.dto.response.MonthlyCollectionStatDTO> countBloodUnitsByMonth(
                        @org.springframework.data.repository.query.Param("year") int year);

        @org.springframework.data.jpa.repository.Query("SELECT t FROM TuiMauEntity t JOIN t.khoMau k " +
                        "WHERE t.trangThai = com.Nhom20.DoAnPhamMem.enums.TrangThaiTuiMau.NHAP_KHO " +
                        "AND (:search IS NULL OR t.maTuiMau LIKE %:search%) " +
                        "AND (:bloodType IS NULL OR k.nhomMau = :bloodType)")
        org.springframework.data.domain.Page<TuiMauEntity> searchAndFilterBloodUnits(
                        @org.springframework.data.repository.query.Param("search") String search,
                        @org.springframework.data.repository.query.Param("bloodType") com.Nhom20.DoAnPhamMem.enums.NhomMau bloodType,
                        org.springframework.data.domain.Pageable pageable);

        @org.springframework.data.jpa.repository.Query(value = "SELECT MAX(CAST(SUBSTRING(maTuiMau, 3) AS UNSIGNED)) FROM tuimau", nativeQuery = true)
        Integer findMaxMaTuiMau();
}

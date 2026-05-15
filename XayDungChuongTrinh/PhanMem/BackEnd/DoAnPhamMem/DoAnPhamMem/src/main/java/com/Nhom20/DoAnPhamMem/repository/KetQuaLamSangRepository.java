package com.Nhom20.DoAnPhamMem.repository;

import com.Nhom20.DoAnPhamMem.entity.KetQuaLamSangEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KetQuaLamSangRepository extends JpaRepository<KetQuaLamSangEntity, String> {

    // Tìm kết quả khám theo mã đơn đăng ký
    Optional<KetQuaLamSangEntity> findByDonDangKy_MaDon(String maDon);

    // Lấy danh sách maDon đã có kết quả khám (để disable nút Khám trên frontend)
    @Query("SELECT k.donDangKy.maDon FROM KetQuaLamSangEntity k")
    List<String> findAllMaDonDaKham();

    // Sinh mã KQ mới: tìm max số trong cột maKQ (format "KQ" + số 5 chữ số)
    @Query(value = "SELECT MAX(CAST(SUBSTRING(maKQ, 3) AS UNSIGNED)) FROM KETQUALAMSANG", nativeQuery = true)
    Integer findMaxMaKQ();
}

package com.Nhom20.DoAnPhamMem.entity;

import com.Nhom20.DoAnPhamMem.enums.TheTich;
import com.Nhom20.DoAnPhamMem.enums.TrangThaiDonDangKy;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DONDANGKY")
@Getter
@Setter
public class DonDangKyEntity {
    @Id
    @Column(name = "maDon", length = 10)
    private String maDon;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maTNV")
    private TinhNguyenVienEntity tinhNguyenVien;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maChienDich")
    private ChienDichHienMauEntity chienDich;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVienEntity nhanVienPhuTrach;
    @Column(name = "maQR", length = 255)
    private String maQR;
    @Column(name = "thoiGianDangKy")
    private LocalDateTime thoiGianDangKy;
    @Column(name = "trangThai", length = 50)
    private TrangThaiDonDangKy trangThai;
    @Column(name = "theTich")
    private TheTich theTich;
}

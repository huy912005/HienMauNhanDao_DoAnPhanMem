package com.Nhom20.DoAnPhamMem.entity;

import com.Nhom20.DoAnPhamMem.enums.GioiTinh;
import com.Nhom20.DoAnPhamMem.enums.NhomMau;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "TINHNGUYENVIEN")
@Getter
@Setter
public class TinhNguyenVienEntity {
    @Id
    @Column(name = "maTNV", length = 10)
    private String maTNV;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maTaiKhoan")
    private TaiKhoanEntity taiKhoan;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maPhuongXa")
    private PhuongXaEntity phuongXa;
    @Column(name = "hoTen", nullable = false, length = 100)
    private String hoTen;
    @Column(name = "CCCD", nullable = false, length = 12)
    private String cccd;
    @Column(name = "ngaySinh", nullable = false)
    private LocalDate ngaySinh;
    @Column(name = "gioiTinh", length = 10)
    private GioiTinh gioiTinh;
    @Column(name = "soDienThoai", nullable = false, length = 10)
    private String soDienThoai;
    @Column(name = "nhomMau", length = 5)
    private NhomMau nhomMau;
    @Column(name = "diaChi", length = 255)
    private String diaChi;
}

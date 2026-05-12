package com.Nhom20.DoAnPhamMem.entity;



import com.Nhom20.DoAnPhamMem.enums.GioiTinh;

import jakarta.persistence.*;

import lombok.*;



@Entity

@Table(name = "NHANVIEN")

@Getter

@Setter

@NoArgsConstructor

public class NhanVienEntity {

    @Id

    @Column(name = "maNhanVien", length = 10)

    private String maNhanVien;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "maTaiKhoan")

    private TaiKhoanEntity taiKhoan;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "maKhoa")

    private KhoaCongTacEntity khoaCongTac;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "maDiaDiem")

    private DiaDiemEntity diaDiem;

    @Column(name = "hoTen", nullable = false, length = 100)

    private String hoTen;

    @Column(name = "CCCD", nullable = false, length = 12)

    private String cccd;

    @Column(name = "gioiTinh", length = 10)

    private GioiTinh gioiTinh;

    @Column(name = "soDienThoai", nullable = false, length = 10)

    private String soDienThoai;

}


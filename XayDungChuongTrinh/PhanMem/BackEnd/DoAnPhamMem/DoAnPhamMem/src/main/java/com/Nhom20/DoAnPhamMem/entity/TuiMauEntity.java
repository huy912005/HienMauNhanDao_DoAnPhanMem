package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TUIMAU")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuiMauEntity {

    @Id
    @Column(name = "maTuiMau", length = 7)
    private String maTuiMau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maDon")
    private DonDangKyEntity donDangKy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maNhanVien")
    private NhanVienEntity nhanVien;

    @Column(name = "maKho", length = 7)
    private String maKho;

    private Integer theTich;

    private LocalDateTime thoiGianLayMau;

    private String trangThai;

    private Double nhietDoVanChuyen;
}
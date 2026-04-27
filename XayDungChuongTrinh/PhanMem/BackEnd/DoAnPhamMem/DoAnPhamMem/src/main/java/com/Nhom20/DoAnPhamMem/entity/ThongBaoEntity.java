package com.Nhom20.DoAnPhamMem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "THONGBAO")
@Getter
@Setter
@NoArgsConstructor
public class ThongBaoEntity {
    @Id
    @Column(name = "maThongBao", length = 10)
    private String maThongBao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maTaiKhoanGui")
    private TaiKhoanEntity nguoiGui;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maTaiKhoanNhan")
    private TaiKhoanEntity nguoiNhan;
    private String noiDung;
    private LocalDateTime thoiGianGui = LocalDateTime.now();
    private String trangThai;
}
